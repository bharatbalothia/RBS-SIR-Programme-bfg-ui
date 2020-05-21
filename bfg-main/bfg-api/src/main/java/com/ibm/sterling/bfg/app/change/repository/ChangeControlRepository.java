package com.ibm.sterling.bfg.app.change.repository;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeControlRepository extends JpaRepository<ChangeControl, String> {

}
