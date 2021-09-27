package com.ibm.sterling.bfg.app.service.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForApprovalException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.exception.entity.EntityApprovalException;
import com.ibm.sterling.bfg.app.exception.entity.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.audit.EventType;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.*;
import com.ibm.sterling.bfg.app.model.validation.EntityValidationComponent;
import com.ibm.sterling.bfg.app.model.validation.unique.EntityFieldName;
import com.ibm.sterling.bfg.app.repository.entity.EntityRepository;
import com.ibm.sterling.bfg.app.service.GenericSpecification;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.ACCEPTED;
import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.PENDING;
import static com.ibm.sterling.bfg.app.model.entity.EntityParticipantType.DIRECT;
import static com.ibm.sterling.bfg.app.model.entity.EntityParticipantType.INDIRECT;
import static com.ibm.sterling.bfg.app.model.entity.EntityService.GPL;
import static com.ibm.sterling.bfg.app.model.entity.EntityService.SCT;

@Service
@Transactional(readOnly = true)
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
    private AdminAuditService adminAuditService;

    @Autowired
    private EntityValidationComponent entityValidation;

    @Override
    public boolean existsByServiceAndEntity(String service, String entity) {
        LOG.info("exists by {} and {}", service, entity);
        return entityRepository.existsByServiceAndEntityAllIgnoreCase(service, entity);
    }

    @Override
    public boolean existsByMailboxPathOut(String mailboxPathOut) {
        LOG.info("exists by mailboxPathOut: {}", mailboxPathOut);
        return entityRepository.existsByMailboxPathOut(mailboxPathOut);
    }

    @Override
    public boolean existsByMqQueueOut(String mqQueueOut) {
        LOG.info("exists by mqQueueOut: {}", mqQueueOut);
        return entityRepository.existsByMqQueueOut(mqQueueOut);
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
    @Transactional
    public Entity save(Entity entity) {
        LOG.info("Trying to save entity {}", entity);
        ChangeControl changeControl = new ChangeControl();
        entity.setChangeID(changeControl.getChangeID());
        Entity savedEntity = entityRepository.save(entity);
        LOG.info("Saved entity {}", savedEntity);
        return entity;
    }

    @Transactional
    public Entity saveEntityToChangeControl(Entity entity, Operation operation) {
        changeControlService.checkOnPendingState(entity.getEntity(), entity.getService());
        if (!operation.equals(Operation.DELETE)) {
            entityValidation.validateEntity(entity, operation);
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
        adminAuditService.fireAdminAuditEvent(new AdminAuditEventRequest(changeControl, changeControl.getChanger()));
        return entity;
    }

    @Transactional
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
        adminAuditService.fireAdminAuditEvent(new AdminAuditEventRequest(changeControl, changeControl.getApprover()));
        return entity;
    }

    private Entity approveEntity(ChangeControl changeControl) throws JsonProcessingException {
        LOG.info("Approve the Entity " + changeControl.getOperation() + " action");
        Entity entity = changeControl.convertEntityLogToEntity();
        Operation operation = changeControl.getOperation();
        checkParticipantOnApproval(entity, operation);
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

        if (!operation.equals(Operation.DELETE)) {
            entityValidation.validateEntity(entity, operation);
        } else {
            entity.setDeleted(Boolean.TRUE);
            entity.setService("DEL_" + entity.getEntityId() + "_" + entity.getService());
            entity.setMailboxPathOut("DEL_" + entity.getEntityId() + "_" + entity.getMailboxPathOut());
            entity.setMqQueueOut("DEL_" + entity.getEntityId() + "_" + entity.getMqQueueOut());
        }

        SWIFTNetRoutingRuleServiceResponse routingRules = new SWIFTNetRoutingRuleServiceResponse();
        if (GPL.name().equals(changeControl.getEntityLog().getService())) {
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

    private void checkParticipantOnApproval(Entity entity, Operation operation) {
        if (entity.getService().equals(SCT.name())) {
            if (entity.getEntityParticipantType().equals(INDIRECT.name())) {
                if (!findEntityNameForParticipants(entity.getEntityId())
                        .contains(entity.getDirectParticipant()))
                    throw new EntityApprovalException(null,
                            "You cannot approve the change to this entity, because the entity does not have " +
                            "a valid direct participant");
            }
            String indirectEntitiesReferredToParticularEntity = entityRepository.
                    findByServiceAndDirectParticipantAndEntityParticipantTypeAndDeletedOrderByEntityAsc(
                    SCT.name(), entity.getEntity(), INDIRECT.name(), false)
                    .stream()
                    .map(Entity::getEntity)
                    .collect(Collectors.joining(", "));
            if (!indirectEntitiesReferredToParticularEntity.isEmpty()) {
                if (operation.equals(Operation.DELETE) && entity.getEntityParticipantType().equals(DIRECT.name()))
                    throw new EntityApprovalException(null,
                            "You cannot approve deletion of this entity, because it is the direct participant " +
                            "for the following entities: " + indirectEntitiesReferredToParticularEntity);
                if (operation.equals(Operation.UPDATE) && entity.getEntityParticipantType().equals(INDIRECT.name()))
                    throw new EntityApprovalException(null,
                            "You cannot approve the participant type change to this entity, because it is " +
                            "the direct participant for the following entities: "
                            + indirectEntitiesReferredToParticularEntity);
            }
        }
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
        Specification<Entity> specification = getExistingEntitySpecificationByService(service)
                .and(
                        GenericSpecification.filter("entity", entity))
                .and(
                        GenericSpecification.filter("swiftDN", swiftDN)
                );
        List<Entity> entities = entityRepository.findAll(specification);
        List<ChangeControl> controls = changeControlService.findPendingChangeControls(entity, service, swiftDN);
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
        return getEntitiesAsc(getExistingEntitySpecificationByService(service));
    }

    private Specification<Entity> getExistingEntitySpecificationByService(String service) {
        Specification<Entity> specification = Specification
                .where(
                        GenericSpecification.filter("deleted", "false"));
        if (service.equals("")) {
            return specification
                    .and(Specification
                            .where(
                                    GenericSpecification.<Entity>filter("service", SCT.name())
                            .or(
                                    GenericSpecification.filter("service", GPL.name()))));
        } else {
            return specification
                    .and(
                            GenericSpecification.<Entity>filter("service", service));
        }
    }

    private List<Entity> getEntitiesAsc(Specification<Entity> specification) {
        return entityRepository.findAll(specification, Sort.by(Sort.Order.asc("entity").ignoreCase()));
    }

    @Override
    public List<String> findEntityNameForParticipants(Integer entityId) {
        return getEntitiesAsc(
                getExistingEntitySpecificationByService(SCT.name())
                        .and(GenericSpecification.equals("entityParticipantType", "DIRECT"))
        ).stream()
                .filter(entity -> !entity.getEntityId().equals(entityId))
                .map(Entity::getEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Entity getEntityWithAttributesOfRoutingRules(String inboundRequestorDN, String inboundResponderDN,
                                                        String inboundService, List<String> inboundRequestType) {
        LOG.info("Routing rule attributes: inboundRequestorDN - {}, inboundResponderDN - {}, inboundService - {}, inboundRequestType - {}",
                inboundRequestorDN, inboundResponderDN, inboundService, inboundRequestType);
        List<Entity> entities = entityRepository.findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCase(
                inboundRequestorDN, inboundResponderDN, inboundService, false);
        return entities.stream()
                .filter(entity -> !Collections.disjoint(entity.getInboundRequestType(),
                        Optional.ofNullable(inboundRequestType).orElse(new ArrayList<>())))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Entity getEntityWithAttributesOfRoutingRulesBesidesItself(String inboundRequestorDN, String inboundResponderDN,
                                                                     String inboundService, List<String> inboundRequestType, Integer entityId) {
        LOG.info("Routing rule attributes: inboundRequestorDN - {}, inboundResponderDN - {}, inboundService - {}, " +
                        "inboundRequestType - {}, entityId - {}",
                inboundRequestorDN, inboundResponderDN, inboundService, inboundRequestType, entityId);
        List<Entity> entities = entityRepository.
                findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCaseAndEntityIdNot(
                inboundRequestorDN, inboundResponderDN, inboundService, false, entityId);
        return entities.stream()
                .filter(entity -> !Collections.disjoint(entity.getInboundRequestType(),
                        Optional.ofNullable(inboundRequestType).orElse(new ArrayList<>())))
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional
    public void updatePendingEntity(ChangeControl changeControl, Entity entity) {
        String currentName = changeControl.getResultMeta1();
        String newName = entity.getEntity();
        String actionValue = currentName.equals(newName) ? currentName : currentName + " -> " + newName;
        changeControlService.updateChangeControl(changeControl, entity);
        adminAuditService.fireAdminAuditEvent(
                new AdminAuditEventRequest(changeControl, EventType.REQUEST_EDITED, actionValue));
    }

    @Override
    @Transactional
    public void cancelPendingEntity(ChangeControl changeControl) {
        changeControlService.deleteChangeControl(changeControl);
        adminAuditService.fireAdminAuditEvent(
                new AdminAuditEventRequest(changeControl, EventType.REQUEST_CANCELLED, changeControl.getResultMeta1()));
    }
}
