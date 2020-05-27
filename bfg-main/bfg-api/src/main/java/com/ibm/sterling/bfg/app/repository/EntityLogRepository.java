package com.ibm.sterling.bfg.app.repository;

import com.ibm.sterling.bfg.app.model.EntityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityLogRepository extends JpaRepository<EntityLog, Integer> {
}
