package com.ibm.sterling.bfg.app.repository;

import com.ibm.sterling.bfg.app.model.Entity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EntityRepository extends JpaRepository<Entity, Integer>, JpaSpecificationExecutor<Entity> {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByMailboxPathOut(String mailboxPathOut);

    boolean existsByServiceAndEntityAllIgnoreCase(String service, String entity);

    Page<Entity> findByDeleted(boolean deleted, Pageable pageable);
//    List<Entity> findByDeleted(boolean deleted);
    List<Entity> findByDeleted(Specification<Entity> specification);
//    @Query("select ent from Entity ent where deleted=false")
    List<Entity> findAll(Specification<Entity> specification);


    Page<Entity> findByServiceIgnoreCaseAndDeleted(String service, boolean deleted, Pageable pageable);
    List<Entity> findByServiceIgnoreCaseAndDeleted(String service, boolean deleted);

    Page<Entity> findByEntityContainingIgnoreCaseAndDeleted(String entity, boolean deleted, Pageable pageable);
    List<Entity> findByEntityContainingIgnoreCaseAndDeleted(String entity, boolean deleted);

    List<Entity> findByEntityContainingIgnoreCaseAndServiceContainingIgnoreCaseAndDeleted(String entity, String service, boolean deleted);
}
