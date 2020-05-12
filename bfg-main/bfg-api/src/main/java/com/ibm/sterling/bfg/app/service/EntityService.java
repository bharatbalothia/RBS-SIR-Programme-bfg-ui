package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntityService {
    List<Entity> listAll();

    Optional<Entity> findById(int id);

    void deleteById(int id);

    Entity save(Entity entity);

    Page<Entity> findEntities(Pageable pageable);

    Page<Entity> findEntitiesByService(String service, Pageable pageable);

    Page<Entity> findEntitiesByEntity(String entity, Pageable pageable);
}
