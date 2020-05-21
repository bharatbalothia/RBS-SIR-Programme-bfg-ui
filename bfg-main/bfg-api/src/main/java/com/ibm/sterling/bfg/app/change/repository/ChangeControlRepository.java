package com.ibm.sterling.bfg.app.change.repository;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import com.ibm.sterling.bfg.app.change.model.ChangeControlStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChangeControlRepository extends JpaRepository<ChangeControl, String> {
    List<ChangeControl> findByStatus(ChangeControlStatus status);
}
