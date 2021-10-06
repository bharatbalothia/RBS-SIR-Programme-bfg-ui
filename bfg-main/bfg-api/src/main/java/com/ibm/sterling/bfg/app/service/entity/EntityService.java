package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.EntityType;
import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntityService extends FieldValueExists {

    boolean existsByServiceAndEntity(String service, String entity);

    List<Entity> listAll();

    Optional<Entity> findById(int id);

    String findNameById(int id);

//    Entity save(Entity entity);

    Entity getEntityAfterApprove(ChangeControl changeControl, String approverComments, ChangeControlStatus status) throws Exception;

    Entity saveEntityToChangeControl(Entity entity, Operation operation);

    Page<EntityType> findEntities(Pageable pageable, String entity, String service, String swiftDN);

    boolean fieldValueExistsBesidesItself(Integer entityId, Object value, String fieldName) throws UnsupportedOperationException;

    List<Entity> findEntitiesByService(String service);

    List<String> findEntityNameForParticipants(Integer entityId);

    Entity getEntityWithAttributesOfRoutingRules(String inboundRequestorDN, String inboundResponderDN,
                                                 String inboundService, List<String> inboundRequestType);

    Entity getEntityWithAttributesOfRoutingRulesBesidesItself(String inboundRequestorDN, String inboundResponderDN,
                                                              String inboundService, List<String> inboundRequestType,
                                                              Integer entityId);

    void updatePendingEntity(ChangeControl changeControl, Entity entity);

    void cancelPendingEntity(ChangeControl changeControl);

    boolean existsByMailboxPathOut(String mailboxPathOut);

    boolean existsByMqQueueOut(String mqQueueOut);
}
