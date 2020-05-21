package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.change.model.ChangeViewer;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntityService extends FieldValueExists {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByServiceAndEntity(String service, String entity);

    List<Entity> listAll();

    Optional<Entity> findById(int id);

    void deleteById(int id);

    Entity save(Entity entity);

    public Entity getEntityAfterApprove(ChangeViewer changeViewer) throws Exception;

    Entity saveEntityToChangeControl(Entity entity);

//    public Entity saveEntityAfterApprove(ChangeViewer changeViewer) throws Exception;

    Page<Entity> findEntities(Pageable pageable);

    Page<Entity> findEntitiesByService(String service, Pageable pageable);

    Page<Entity> findEntitiesByEntity(String entity, Pageable pageable);

}
