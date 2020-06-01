package com.ibm.sterling.bfg.app.repository;

import com.ibm.sterling.bfg.app.model.Entity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntityRepository extends JpaRepository<Entity, Integer>, JpaSpecificationExecutor<Entity> {

    boolean existsByMqQueueOut(String mqQueueOut);

    boolean existsByMailboxPathOut(String mailboxPathOut);

    boolean existsByServiceAndEntityAllIgnoreCase(String service, String entity);

    List<Entity> findAll(Specification<Entity> specification);
}
