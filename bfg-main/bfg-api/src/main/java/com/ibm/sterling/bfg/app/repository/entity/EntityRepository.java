package com.ibm.sterling.bfg.app.repository.entity;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityRepository extends JpaRepository<Entity, Integer>, JpaSpecificationExecutor<Entity> {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByMailboxPathOut(String mailboxPathOut);

    boolean existsByServiceAndEntityAllIgnoreCase(String service, String entity);

    @EntityGraph(value = "graph.Entity.schedules")
    List<Entity> findAll(Specification<Entity> specification);

    @EntityGraph(value = "graph.Entity.schedules")
    List<Entity> findByDeleted(boolean deleted);

    boolean existsByMqQueueOutAndDeletedAndEntityIdNot(String mqQueueOut, Boolean deleted, Integer entityId);

    boolean existsByMailboxPathOutAndDeletedAndEntityIdNot(String mailboxPathOut, Boolean deleted, Integer entityId);

    boolean existsByEntityAndServiceAndDeletedAndEntityIdNot(String entity, String service, Boolean deleted, Integer entityId);

    List<Entity> findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAllIgnoreCase(
            String inboundRequestorDN, String inboundResponderDN, String inboundService);

    List<Entity> findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAllIgnoreCaseAndEntityIdNot(
            String inboundRequestorDN, String inboundResponderDN, String inboundService, Integer entityId);

}
