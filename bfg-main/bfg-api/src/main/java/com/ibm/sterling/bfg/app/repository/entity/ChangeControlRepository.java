package com.ibm.sterling.bfg.app.repository.entity;

import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeControlRepository extends JpaRepository<ChangeControl, String>, JpaSpecificationExecutor<ChangeControl> {

    List<ChangeControl> findByStatus(ChangeControlStatus status);

    List<ChangeControl> findAll(Specification<ChangeControl> specification);

    Optional<ChangeControl> findByChangeIDAndStatus(String changeID, ChangeControlStatus status);

}
