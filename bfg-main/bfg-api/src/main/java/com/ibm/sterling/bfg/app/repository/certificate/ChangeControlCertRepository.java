package com.ibm.sterling.bfg.app.repository.certificate;

import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeControlCertRepository extends JpaRepository<ChangeControlCert, String> {
    List<ChangeControlCert> findByStatus(ChangeControlStatus status);

    List<ChangeControlCert> findAll(Specification<ChangeControlCert> specification);

    boolean existsByResultMeta2AndStatus(String resultMeta2, ChangeControlStatus changeControlStatus);
}
