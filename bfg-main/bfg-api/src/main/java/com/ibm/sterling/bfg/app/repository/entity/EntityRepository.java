package com.ibm.sterling.bfg.app.repository.entity;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface EntityRepository extends JpaRepository<Entity, Integer>, JpaSpecificationExecutor<Entity> {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByMailboxPathOut(String mailboxPathOut);

    boolean existsByServiceAndEntityAllIgnoreCase(String service, String entity);

    @EntityGraph(attributePaths = "schedules")
    List<Entity> findAll(Specification<Entity> specification);

    @EntityGraph(attributePaths = "schedules")
    List<Entity> findAll(Specification<Entity> specification, Sort sort);

    @EntityGraph(attributePaths = "schedules")
    Optional<Entity> findById(Integer entityId);

    boolean existsByMqQueueOutAndDeletedAndEntityIdNot(String mqQueueOut, Boolean deleted, Integer entityId);

    boolean existsByMailboxPathOutAndDeletedAndEntityIdNot(String mailboxPathOut, Boolean deleted, Integer entityId);

    boolean existsByEntityAndServiceAndDeletedAndEntityIdNot(String entity, String service, Boolean deleted, Integer entityId);

    List<Entity> findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCase(
            String inboundRequestorDN, String inboundResponderDN, String inboundService, Boolean deleted);

    List<Entity> findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCaseAndEntityIdNot(
            String inboundRequestorDN, String inboundResponderDN, String inboundService, Boolean deleted, Integer entityId);

    List<Entity> findByServiceAndDirectParticipantAndEntityParticipantTypeAndDeletedOrderByEntityAsc(
            String service, String directParticipant,String entityParticipantType, Boolean deleted);
}
