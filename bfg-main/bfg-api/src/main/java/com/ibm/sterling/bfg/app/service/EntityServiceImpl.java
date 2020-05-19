package com.ibm.sterling.bfg.app.service;


import com.ibm.sterling.bfg.app.model.Entity;
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
        return entityRepository.save(entity);
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
