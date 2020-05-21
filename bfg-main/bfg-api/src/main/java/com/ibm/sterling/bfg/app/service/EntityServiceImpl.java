package com.ibm.sterling.bfg.app.service;


import com.ibm.sterling.bfg.app.change.model.*;
import com.ibm.sterling.bfg.app.change.service.ChangeControlService;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityLog;
import com.ibm.sterling.bfg.app.repository.EntityLogRepository;
import com.ibm.sterling.bfg.app.repository.EntityRepository;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntityServiceImpl implements EntityService {

    private static final Logger LOG = LogManager.getLogger(EntityServiceImpl.class);

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private ChangeControlService changeControlService;

    @Autowired
    private EntityLogRepository entityLogRepository;

    @Override
    public boolean existsByMqQueueOut(String mqQueueOut) {
        LOG.info("exists by mqQueueOut {}", mqQueueOut);
        return entityRepository.existsByMqQueueOut(mqQueueOut);
    }

    @Override
    public boolean existsByServiceAndEntity(String service, String entity) {
        LOG.info("exists by {} and {}", service, entity);
        return entityRepository.existsByServiceAndEntity(service, entity);
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
        LOG.debug("Entity saving");
        LOG.debug("Trying to save entity {}", entity);
        ChangeControl changeControl = new ChangeControl();
        entity.setChangeID(changeControl.getChangeID());
        Entity savedEntity = entityRepository.save(entity);
        LOG.debug("Saved entity {}", savedEntity);
        return entity;
    }

    public Entity saveEntityToChangeControl(Entity entity) {
        LOG.debug("Trying to save entity to change control:" + entity);
        ChangeControl changeControl = new ChangeControl();
//        try {
//            changeControlService.save(changeControl);
//        } catch (Exception e) {
//            LOG.error("Error persisting the Change Control record: " + e.getMessage());
//            e.printStackTrace();
//        }
//        LOG.debug("Change control id:" + changeControl.getChangeID());
//        entity.setChangeID(changeControl.getChangeID());
        changeControl.setOperation(Operation.CREATE);
        changeControl.setChanger("TEST_USER");
        changeControl.setChangerComments(entity.getChangerComments());
        changeControl.setResultMeta1(entity.getEntity());
        changeControl.setResultMeta2(entity.getService());
        changeControl.setEntityLog(new EntityLog(entity));
        try {
            entity.setChangeID(changeControlService.save(changeControl).getChangeID());
        } catch (Exception e) {
            LOG.error("Error persisting the Change Control record: " + e.getMessage());
            LOG.error("The Entity could not be saved " + entity);
            e.printStackTrace();
        }
        return entity;
    }

    public Entity getEntityAfterApprove(ChangeViewer changeViewer) throws Exception {
        ChangeControl changeControl = changeViewer.getChange();
        if(changeControl.getStatus() != ChangeControlStatus.PENDING){
            throw new Exception("Status is not pending and therefore no action can be taken");
        }
        Entity entity = new Entity();
        Operation operation = changeControl.getOperation();
        switch (operation) {
            case CREATE :
                entity = saveEntityAfterApprove(changeControl);
                break;
            case UPDATE:
            case DELETE:
        }
        return entity;
    }

    private Entity saveEntityAfterApprove(ChangeControl changeControl) {
        LOG.debug("Approve the Entity create action");
        Entity savedEntity = entityRepository.save(changeControl.getEntityFromEntityLog());
        LOG.debug("Saved entity to DB {}", savedEntity);
        changeControlService.setApproveInfo(
                changeControl,
                "TEST_APPROVER",
                "TEST_APPROVE_COMMENTS",
                ChangeControlStatus.ACCEPTED);
        EntityLog entityLog = changeControl.getEntityLog();
        entityLog.setEntityId(savedEntity.getEntityId());
        changeControl.setEntityLog(entityLog);
        try {
            changeControlService.save(changeControl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedEntity;
    }

    @Override
    public Page<Entity> findEntities(Pageable pageable) {
        LOG.info("all existing entities");
        return entityRepository.findByDeleted(false, pageable);
    }

    @Override
    public Page<Entity> findEntitiesByService(String service, Pageable pageable) {
        LOG.info("existing entities by service");
        return entityRepository.findByServiceIgnoreCaseAndDeleted(service, false, pageable);
    }

    @Override
    public Page<Entity> findEntitiesByEntity(String entity, Pageable pageable) {
        LOG.info("existing entities by name");
        return entityRepository.findByEntityContainingIgnoreCaseAndDeleted(entity, false, pageable);
    }

    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (fieldName.equals("MQQUEUEOUT"))
            return this.entityRepository.existsByMqQueueOut(value.toString());
        if (fieldName.equals("MAILBOXPATHOUT"))
            return this.entityRepository.existsByMailboxPathOut(value.toString());
        return false;
    }

}
