package com.ibm.sterling.bfg.app.change.repository;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import com.ibm.sterling.bfg.app.change.model.ChangeControlStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface ChangeControlRepository extends JpaRepository<ChangeControl, String> {

}
