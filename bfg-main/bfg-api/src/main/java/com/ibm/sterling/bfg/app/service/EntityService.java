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
public class EntityService {
    private static final Logger LOG = LogManager.getLogger(EntityService.class);

    @Autowired
    private EntityRepository entityRepository;

    public List<Entity> listAll() {
        return entityRepository.findAll();
    }

    public Optional<Entity> findById(int id) {
        LOG.info("entity by id {}", id);
        return entityRepository.findById(id);
    }

    public void deleteById(int id) {
        LOG.info("deleting entity by id {}", id);
        entityRepository.deleteById(id);
    }

    public Entity save(Entity entity) {
        return entityRepository.save(entity);
    }

    public Page<Entity> findEntities(Pageable pageable) {
        LOG.info("all existing entities");
        return entityRepository.findByDeleted(false, pageable);
    }

    public Page<Entity> findEntitiesByService(String service, Pageable pageable) {
        LOG.info("existing entities by service");
        return entityRepository.findByServiceIgnoreCaseAndDeleted(service, false, pageable);
    }

    public Page<Entity> findEntitiesByEntity(String entity, Pageable pageable) {
        LOG.info("existing entities by name");
        return entityRepository.findByEntityContainingIgnoreCaseAndDeleted(entity, false, pageable);
    }

}
