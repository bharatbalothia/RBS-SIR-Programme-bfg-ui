package com.ibm.sterling.bfg.app.repository.entity;

import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
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
public interface ChangeControlRepository extends JpaRepository<ChangeControl, String>, JpaSpecificationExecutor<ChangeControl> {

    @EntityGraph(attributePaths = "entityLog")
    List<ChangeControl> findAll(Specification<ChangeControl> specification);

    @EntityGraph(attributePaths = "entityLog")
    Optional<ChangeControl> findByChangeIDAndStatus(String changeID, ChangeControlStatus status);

    @EntityGraph(attributePaths = "entityLog")
    Optional<ChangeControl> findById(String changeID);

}
