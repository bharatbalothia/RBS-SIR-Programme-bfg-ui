package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
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

    public Entity getEntityAfterApprove(String changeId, String approverComments, ChangeControlStatus status) throws Exception;

    Entity saveEntityToChangeControl(Entity entity);

    Page<Object> findEntities(Pageable pageable, String entity, String service);

    Page<Object> findEntitiesByService(String service, Pageable pageable);

    Page<Object> findEntitiesByEntity(String entity, Pageable pageable);

    Page<Object> findEntitiesByEntityAndService(String entityName, String serviceName, Pageable pageable);
}
