package com.ibm.sterling.bfg.app.repository;

import com.ibm.sterling.bfg.app.model.Entity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends JpaRepository<Entity, Integer> {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByMailboxPathOut(String mailboxPathOut);

    boolean existsByServiceAndEntityAllIgnoreCase(String service, String entity);

    Page<Entity> findByDeleted(boolean deleted, Pageable pageable);

    Page<Entity> findByServiceIgnoreCaseAndDeleted(String service, boolean deleted, Pageable pageable);

    Page<Entity> findByEntityContainingIgnoreCaseAndDeleted(String entity, boolean deleted, Pageable pageable);

}
