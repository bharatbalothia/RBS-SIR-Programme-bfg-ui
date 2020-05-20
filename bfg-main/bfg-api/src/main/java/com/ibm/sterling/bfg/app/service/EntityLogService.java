package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityLog;
import com.ibm.sterling.bfg.app.repository.EntityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntityLogService {
    @Autowired
    private EntityLogRepository repository;


    public List<Entity> listAll() {
        return null;
    }

    public Optional<Entity> findById(int id) {
        return Optional.empty();
    }

    public void deleteById(int id) {

    }

    public EntityLog save(Entity entity) {
        EntityLog entityLog = new EntityLog(entity);
        return repository.save(entityLog);
    }

    public Entity saveEntityToChangeControl(Entity entity) {
        return null;
    }

    public Page<Entity> findEntities(Pageable pageable) {
        return null;
    }

    public Page<Entity> findEntitiesByService(String service, Pageable pageable) {
        return null;
    }

    public Page<Entity> findEntitiesByEntity(String entity, Pageable pageable) {
        return null;
    }
}
