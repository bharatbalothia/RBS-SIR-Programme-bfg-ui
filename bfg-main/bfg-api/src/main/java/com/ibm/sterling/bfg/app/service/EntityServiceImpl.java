package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityLog;
import com.ibm.sterling.bfg.app.model.EntityType;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.validation.PostValidation;
import com.ibm.sterling.bfg.app.model.validation.PutValidation;
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

@Service
@Transactional
public class EntityServiceImpl implements EntityService {

    private static final Logger LOG = LogManager.getLogger(EntityServiceImpl.class);

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private ChangeControlService changeControlService;

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
        boolean isUnique = getEntitiesExceptCurrent(entity).stream()
                .anyMatch(ent -> ent.getService().equals(entity.getService()) &
                        ent.getEntity().equals(entity.getEntity()));
        LOG.info(isUnique);
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
    public void deleteById(int id) {
        LOG.info("deleting entity by id {}", id);
        entityRepository.deleteById(id);
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

    public Entity saveEntityToChangeControl(Entity entity, Operation operation) {
        LOG.debug("Trying to save entity {} to change control", entity);
        ChangeControl changeControl = new ChangeControl();
        changeControl.setOperation(operation);
        changeControl.setChanger(SecurityContextHolder.getContext().getAuthentication().getName());
        changeControl.setChangerComments(entity.getChangerComments());
        changeControl.setResultMeta1(entity.getEntity());
        changeControl.setResultMeta2(entity.getService());
        changeControl.setEntityLog(new EntityLog(entity));
        try {
            entity.setChangeID(changeControlService.save(changeControl).getChangeID());
        } catch (Exception e) {
            LOG.error("Error persisting the Change Control record: {}", e.getMessage());
            LOG.error("The Entity {} could not be saved", entity);
            e.printStackTrace();
        }
        return changeControl.convertEntityLogToEntity();
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
        try {
            changeControlService.setApproveInfo(
                    changeControl,
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    approverComments,
                    status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    private Entity approve(ChangeControl changeControl) {
        LOG.debug("Entity {} action", changeControl.getOperation());
        Entity entity = new Entity();
        Operation operation = changeControl.getOperation();
        switch (operation) {
            case CREATE:
            case UPDATE:
                entity = saveEntityAfterApprove(changeControl);
                break;
            case DELETE:
        }
        LOG.debug("Entity after {} action: {}", changeControl.getOperation(), entity);
        return entity;
    }

    private Entity saveEntityAfterApprove(ChangeControl changeControl) {
        LOG.debug("Approve the Entity " + changeControl.getOperation() + " action");
        Entity entity = changeControl.convertEntityLogToEntity();
        Set<ConstraintViolation<Entity>> violations = null;
        Operation operation = changeControl.getOperation();
        switch (operation) {
            case CREATE:
                violations = validator.validate(entity, PostValidation.class);
                break;
            case UPDATE:
                violations = validator.validate(entity, PutValidation.class);
                break;
            case DELETE:
        }
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Entity savedEntity = entityRepository.save(entity);
        LOG.info("Saved entity to DB {}", savedEntity);
        EntityLog entityLog = changeControl.getEntityLog();
        entityLog.setEntityId(savedEntity.getEntityId());
        changeControl.setEntityLog(entityLog);
        changeControlService.save(changeControl);
        return savedEntity;
    }

    @Override
    public Page<EntityType> findEntities(Pageable pageable, String entity, String service) {
        LOG.debug("Search entities by entity name {} and service {}", entity, service);
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
        entities.remove(entity.getEntityId());
        return entities;
    }
}
