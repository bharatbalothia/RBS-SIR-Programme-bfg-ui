package com.ibm.sterling.bfg.app.service.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.*;
import com.ibm.sterling.bfg.app.model.entity.EntityType;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.entity.*;
import com.ibm.sterling.bfg.app.model.validation.gplvalidation.GplValidation;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.SctValidation;
import com.ibm.sterling.bfg.app.model.validation.unique.EntityFieldName;
import com.ibm.sterling.bfg.app.repository.entity.EntityRepository;
import com.ibm.sterling.bfg.app.service.GenericSpecification;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus.ACCEPTED;
import static com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus.PENDING;
import static com.ibm.sterling.bfg.app.model.changeControl.Operation.*;

@Service
@Transactional
public class EntityServiceImpl implements EntityService {

    private static final Logger LOG = LogManager.getLogger(EntityServiceImpl.class);

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private ChangeControlService changeControlService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SWIFTNetRoutingRuleService swiftNetRoutingRuleService;

    @Autowired
    private Validator validator;

    @Override
    public boolean existsByServiceAndEntity(String service, String entity) {
        LOG.info("exists by {} and {}", service, entity);
        return entityRepository.existsByServiceAndEntityAllIgnoreCase(service, entity);
    }

    @Override
    public List<Entity> listAll() {
        return entityRepository.findAll();
    }

    @Override
    public Optional<Entity> findById(int id) {
        LOG.info("entity by id {}", id);
        return entityRepository.findById(id);
    }

    @Override
    public String findNameById(int id) {
        LOG.info("entity by id {}", id);
        return entityRepository.findById(id).map(Entity::getEntity)
                .orElseThrow(() -> new EntityNotFoundException("There is no such entity by id " + id));
    }

    @Override
    public Entity save(Entity entity) {
        LOG.info("Trying to save entity {}", entity);
        ChangeControl changeControl = new ChangeControl();
        entity.setChangeID(changeControl.getChangeID());
        Entity savedEntity = entityRepository.save(entity);
        LOG.info("Saved entity {}", savedEntity);
        return entity;
    }

    private Class getEntityValidationGroup(Entity entity, Operation operation) {
        Map<String, Map<Operation, Class>> entityOperationMap = new HashMap<String, Map<Operation, Class>>() {
            {
                put("GPL", new HashMap<Operation, Class>() {
                            {
                                put(CREATE, GplValidation.PostValidation.class);
                                put(UPDATE, GplValidation.PutValidation.class);

                            }
                        }
                );
                put("SCT", new HashMap<Operation, Class>() {
                            {
                                put(CREATE, SctValidation.PostValidation.class);
                                put(UPDATE, SctValidation.PutValidation.class);
                            }
                        }
                );
            }
        };
        return entityOperationMap.get(entity.getService()).get(operation);
    }

    private void validateEntity(Entity entity, Operation operation) {
        Set<ConstraintViolation<Entity>> violations = validator.validate(entity, getEntityValidationGroup(entity, operation));
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Entity saveEntityToChangeControl(Entity entity, Operation operation) {
        if (!operation.equals(Operation.DELETE)) {
            validateEntity(entity, operation);
        }
        LOG.info("Trying to save entity {} to change control", entity);
        ChangeControl changeControl = new ChangeControl();
        changeControl.setOperation(operation);
        changeControl.setChanger(SecurityContextHolder.getContext().getAuthentication().getName());
        changeControl.setChangerComments(entity.getChangerComments());
        changeControl.setResultMeta1(entity.getEntity());
        changeControl.setResultMeta2(entity.getService());
        changeControl.setEntityLog(new EntityLog(entity));
        entity.setChangeID(changeControlService.save(changeControl).getChangeID());
        return entity;
    }

    public Entity getEntityAfterApprove(ChangeControl changeControl, String approverComments, ChangeControlStatus status)
            throws JsonProcessingException {
        if (!PENDING.equals(changeControl.getStatus())) {
            throw new StatusNotPendingException();
        }
        Entity entity = new Entity();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ACCEPTED.equals(status)) {
            if (userName.equals(changeControl.getChanger()))
                throw new InvalidUserForApprovalException();
            entity = approveEntity(changeControl);
        }
        changeControlService.setApproveInfo(changeControl, userName, approverComments, status);
        return entity;
    }

    private Entity approveEntity(ChangeControl changeControl) throws JsonProcessingException {
        LOG.info("Approve the Entity " + changeControl.getOperation() + " action");
        Entity entity = changeControl.convertEntityLogToEntity();
        Optional.ofNullable(entity.getEntityId()).ifPresent(entityId -> {
                    List<Long> deletedScheduleId = getAbsentSchedules(
                            scheduleService.findActualSchedulesByEntityId(entityId), entity.getSchedules());
                    deletedScheduleId.forEach(id -> scheduleService.deleteById(id));
                }
        );
        entity.setSchedules(
                changeControl.getEntityLog()
                        .getSchedules()
                        .stream()
                        .peek(schedule ->
                                schedule.setEntity(entity))
                        .collect(Collectors.toList())
        );
        Operation operation = changeControl.getOperation();
        if (!operation.equals(Operation.DELETE)) {
            validateEntity(entity, operation);
        } else {
            entity.setDeleted(Boolean.TRUE);
            entity.setService("DEL_" + entity.getEntityId() + "_" + entity.getService());
            entity.setMailboxPathOut("DEL_" + entity.getEntityId() + "_" + entity.getMailboxPathOut());
            entity.setMqQueueOut("DEL_" + entity.getEntityId() + "_" + entity.getMqQueueOut());
            entity.setInboundRoutingRule(true);
        }

        SWIFTNetRoutingRuleServiceResponse routingRules = new SWIFTNetRoutingRuleServiceResponse();
        if (entity.getInboundRoutingRule() && changeControl.getEntityLog().getService().equals("GPL")) {
            routingRules = swiftNetRoutingRuleService.executeRoutingRuleOperation(operation, entity, changeControl.getChanger());
        }

        Entity savedEntity = entityRepository.save(entity);
        LOG.info("Saved entity to DB {}", savedEntity);
        EntityLog entityLog = changeControl.getEntityLog();
        entityLog.setEntityId(savedEntity.getEntityId());
        changeControl.setEntityLog(entityLog);
        changeControlService.save(changeControl);
        savedEntity.setRoutingRules(routingRules);
        return savedEntity;
    }

    private List<Long> getAbsentSchedules(List<Schedule> actualSchedules, List<Schedule> newSchedules) {
        List<Long> actualSchedulesId = convertListOfSchedulesToListOfScheduleId(actualSchedules);
        List<Long> newSchedulesId = convertListOfSchedulesToListOfScheduleId(newSchedules);
        actualSchedulesId.removeAll(newSchedulesId);
        return actualSchedulesId;
    }

    private List<Long> convertListOfSchedulesToListOfScheduleId(List<Schedule> scheduleList) {
        return scheduleList
                .stream()
                .map(Schedule::getScheduleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EntityType> findEntities(Pageable pageable, String entity, String service, String swiftDN) {
        LOG.info("Search entities by entity name {} and service {}", entity, service);
        List<EntityType> entityResults = new ArrayList<>();
        Specification<Entity> specification = Specification
                .where(
                        GenericSpecification.<Entity>filter(entity, "entity"))
                .and(
                        GenericSpecification.filter(service, "service"))
                .and(
                        GenericSpecification.filter("false", "deleted"))
                .and(
                        GenericSpecification.filter(swiftDN, "swiftDN")
                );
        List<Entity> entities = entityRepository.findAll(specification);
        List<ChangeControl> controls = changeControlService.findAllPending(entity, service, swiftDN);
        entities.removeIf(dbEntity ->
                controls.stream().anyMatch(changeControl -> changeControl.getResultMeta1().equals(dbEntity.getEntity()))
        );
        entityResults.addAll(entities);
        entityResults.addAll(controls);
        entityResults.sort(Comparator.comparing(EntityType::nameForSorting, String.CASE_INSENSITIVE_ORDER));
        return ListToPageConverter.convertListToPage(entityResults, pageable);
    }

    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (EntityFieldName.MQQUEUEOUT.name().equals(fieldName))
            return entityRepository.existsByMqQueueOut(String.valueOf(value));
        if (EntityFieldName.MAILBOXPATHOUT.name().equals(fieldName))
            return entityRepository.existsByMailboxPathOut(String.valueOf(value));
        return false;
    }

    @Override
    public boolean fieldValueExistsBesidesItself(Integer entityId, Object value, String fieldName) throws UnsupportedOperationException {
        if (EntityFieldName.MQQUEUEOUT.name().equals(fieldName)) {
            return entityRepository.existsByMqQueueOutAndDeletedAndEntityIdNot(String.valueOf(value), false, entityId);
        }
        if (EntityFieldName.MAILBOXPATHOUT.name().equals(fieldName)) {
            return entityRepository.existsByMailboxPathOutAndDeletedAndEntityIdNot(String.valueOf(value), false, entityId);
        }
        if (EntityFieldName.ENTITY_SERVICE.name().equals(fieldName)) {
            Map<String, String> entityServiceMap = new ObjectMapper().convertValue(value, new TypeReference<Map<String, String>>() {
            });
            return entityRepository.existsByEntityAndServiceAndDeletedAndEntityIdNot(
                    entityServiceMap.get("entity"), entityServiceMap.get("service"), false, entityId);
        }
        return false;
    }

    @Override
    public List<Entity> findEntitiesByService(String service) {
        Specification<Entity> specification = Specification
                .where(
                        GenericSpecification.<Entity>filter(Optional.ofNullable(service).orElse(""), "service"))
                .and(
                        GenericSpecification.filter("false", "deleted"));
        List<Entity> entities = new ArrayList<>(entityRepository.findAll(specification));
        entities.sort(Comparator.comparing(Entity::getEntity, String.CASE_INSENSITIVE_ORDER));
        return entities;
    }

    @Override
    public Entity getEntityWithAttributesOfRoutingRules(String inboundRequestorDN, String inboundResponderDN,
                                                        String inboundService, List<String> inboundRequestType) {
        LOG.info("Routing rule attributes: inboundRequestorDN - {}, inboundResponderDN - {}, inboundService - {}, inboundRequestType - {}",
                inboundRequestorDN, inboundResponderDN, inboundService, inboundRequestType);
        List<Entity> entities = entityRepository.findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAllIgnoreCase(
                inboundRequestorDN, inboundResponderDN, inboundService);
        return entities.stream()
                .filter(entity -> !Collections.disjoint(entity.getInboundRequestType(), inboundRequestType))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Entity getEntityWithAttributesOfRoutingRulesBesidesItself(String inboundRequestorDN, String inboundResponderDN,
                                                                     String inboundService, List<String> inboundRequestType, Integer entityId) {
        LOG.info("Routing rule attributes: inboundRequestorDN - {}, inboundResponderDN - {}, inboundService - {}, " +
                        "inboundRequestType - {}, entityId - {}",
                inboundRequestorDN, inboundResponderDN, inboundService, inboundRequestType, entityId);
        List<Entity> entities = entityRepository.findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAllIgnoreCaseAndEntityIdNot(
                inboundRequestorDN, inboundResponderDN, inboundService, entityId);
        return entities.stream()
                .filter(entity -> !Collections.disjoint(entity.getInboundRequestType(), inboundRequestType))
                .findFirst()
                .orElse(null);
    }

}
