package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityLog;
import com.ibm.sterling.bfg.app.model.EntityType;
import com.ibm.sterling.bfg.app.model.Schedule;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.validation.GplValidation;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.SctValidation;
import com.ibm.sterling.bfg.app.repository.EntityRepository;
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

import static com.ibm.sterling.bfg.app.model.changeControl.Operation.CREATE;
import static com.ibm.sterling.bfg.app.model.changeControl.Operation.UPDATE;

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
    private Validator validator;

    @Override
    public boolean existsByMqQueueOut(String mqQueueOut) {
        LOG.info("exists by mqQueueOut {}", mqQueueOut);
        return entityRepository.existsByMqQueueOut(mqQueueOut);
    }

    @Override
    public boolean existsByServiceAndEntityPut(Entity entity) {
        LOG.info("Checking uniqueness entity and service for {}", entity);
        List<Entity> entities = entityRepository.findByDeleted(false);
        boolean isUnique = getEntitiesExceptCurrent(entity).stream()
                .anyMatch(ent -> ent.getService().equals(entity.getService()) &
                        ent.getEntity().equals(entity.getEntity()));
        LOG.info("Uniqueness entity and service is {}", isUnique);
        return isUnique;
    }

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
        Set<ConstraintViolation<Entity>> violations;
        violations = validator.validate(entity, getEntityValidationGroup(entity, operation));
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

    public Entity getEntityAfterApprove(String changeId, String approverComments, ChangeControlStatus status) throws Exception {
        ChangeControl changeControl = changeControlService.findById(changeId)
                .orElseThrow(EntityNotFoundException::new);
        if (changeControl.getStatus() != ChangeControlStatus.PENDING) {
            throw new Exception("Status is not pending and therefore no action can be taken");
        }
        Entity entity = new Entity();
        switch (status) {
            case ACCEPTED:
                entity = approve(changeControl);
                break;
            case FAILED:
            case REJECTED:
        }
        changeControlService.setApproveInfo(
                changeControl,
                SecurityContextHolder.getContext().getAuthentication().getName(),
                approverComments,
                status);
        return entity;
    }

    private Entity approve(ChangeControl changeControl) {
        LOG.info("Entity {} action", changeControl.getOperation());
        Entity entity = new Entity();
//        Operation operation = changeControl.getOperation();
//        switch (operation) {
//            case CREATE:
//            case UPDATE:
                entity = saveEntityAfterApprove(changeControl);
//                break;
//            case DELETE:
//        }
        LOG.info("Entity after {} action: {}", changeControl.getOperation(), entity);
        return entity;
    }

    private Entity saveEntityAfterApprove(ChangeControl changeControl) {
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
        }
        Entity savedEntity = entityRepository.save(entity);
        LOG.info("Saved entity to DB {}", savedEntity);
        EntityLog entityLog = changeControl.getEntityLog();
        entityLog.setEntityId(savedEntity.getEntityId());
        changeControl.setEntityLog(entityLog);
        changeControlService.save(changeControl);
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
    public Page<EntityType> findEntities(Pageable pageable, String entity, String service) {
        LOG.info("Search entities by entity name {} and service {}", entity, service);
        List<EntityType> entities = new ArrayList<>(changeControlService.findAllPending(entity, service));
        Specification<Entity> specification = Specification
                .where(
                        GenericSpecification.<Entity>filter(entity, "entity"))
                .and(
                        GenericSpecification.filter(service, "service"))
                .and(
                        GenericSpecification.filter("false", "deleted")
                );
        entities.addAll(
                entityRepository
                        .findAll(specification));
        entities.sort(Comparator.comparing(o -> o.nameForSorting().toLowerCase()));
        return ListToPageConverter.convertListToPage(entities, pageable);
    }

    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (fieldName.equals("MQQUEUEOUT"))
            return entityRepository.existsByMqQueueOut(String.valueOf(value));
        if (fieldName.equals("MAILBOXPATHOUT"))
            return entityRepository.existsByMailboxPathOut(String.valueOf(value));
        return false;
    }

    @Override
    public boolean fieldValueExistsPut(Entity entity) throws UnsupportedOperationException {
        return getEntitiesExceptCurrent(entity)
                .stream()
                .anyMatch(ent ->
                        Optional.ofNullable(ent.getMqQueueOut()).map(mqOut -> mqOut.equals(entity.getMqQueueOut())).orElse(false)
                );
    }

    private List<Entity> getEntitiesExceptCurrent(Entity entity) {
        List<Entity> entities = entityRepository.findByDeleted(false);
        Entity editedEntity = entityRepository.findById(entity.getEntityId()).orElse(entity);
        entities.remove(editedEntity);
        return entities;
    }

}
