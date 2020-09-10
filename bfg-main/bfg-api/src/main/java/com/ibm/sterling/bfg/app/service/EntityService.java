package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.EntityType;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntityService extends FieldValueExists {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByServiceAndEntityPut(Entity entity);

    boolean existsByServiceAndEntity(String service, String entity);

    List<Entity> listAll();

    Optional<Entity> findById(int id);

    String findNameById(int id);

    Entity save(Entity entity);

    Entity getEntityAfterApprove(ChangeControl changeControl, String approverComments, ChangeControlStatus status) throws Exception;

    Entity saveEntityToChangeControl(Entity entity, Operation operation);

    Page<EntityType> findEntities(Pageable pageable, String entity, String service);

    boolean fieldValueExistsPut(Entity entity) throws UnsupportedOperationException;

    boolean fieldValueExistsBesidesItself(Integer entityId, Object value, String fieldName) throws UnsupportedOperationException;

    List<Entity> findEntitiesByService(String service);
}
