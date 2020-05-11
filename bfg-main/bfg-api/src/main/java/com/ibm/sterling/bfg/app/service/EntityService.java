package com.ibm.sterling.bfg.app.service;


import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.repository.EntityRepository;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EntityService {
    private static final Logger log = LogManager.getLogger(EntityService.class);
    @Autowired
    private EntityRepository entityRepository;

    public List<Entity> listAll() {
        return entityRepository.findAll();
    }

    public void save(Entity bank) {
        entityRepository.save(bank);
    }

    public Entity get(int id) {
        log.info("entity by id {}", id);
        return entityRepository.findById(id).get();
    }

    public void delete(int id) {
        entityRepository.deleteById(id);
    }

    public Page<Entity> findEntities(Pageable pageable) {
        log.info("all existing entities");
        return entityRepository.findByDeleted(false, pageable);
    }

    public Page<Entity> findEntitiesByService(String service, Pageable pageable) {
        log.info("existing entities by service");
        return entityRepository.findByServiceIgnoreCaseAndDeleted(service, false, pageable);
    }

    public Page<Entity> findEntitiesByEntity(String entity, Pageable pageable) {
        log.info("existing entities by name");
        return entityRepository.findByEntityContainingIgnoreCaseAndDeleted(entity, false, pageable);
    }

}
