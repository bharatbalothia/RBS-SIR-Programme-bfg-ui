package com.ibm.sterling.bfg.app.repository;

import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChangeControlRepository extends JpaRepository<ChangeControl, String> {
    List<ChangeControl> findByStatus(ChangeControlStatus status);
    List<ChangeControl> findByStatusAndResultMeta1(ChangeControlStatus status, String entity);
    List<ChangeControl> findByStatusAndResultMeta2(ChangeControlStatus status, String service);
}
