package com.ibm.sterling.bfg.app.repository.certificate;

import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChangeControlCertRepository extends JpaRepository<ChangeControlCert, String> {

    @EntityGraph(attributePaths = "trustedCertificateLog")
    @Transactional(readOnly = true)
    List<ChangeControlCert> findAll(Specification<ChangeControlCert> specification);

    ChangeControlCert findByResultMeta1AndStatus(String resultMeta1, ChangeControlStatus changeControlStatus);

    boolean existsByResultMeta1AndStatus(String resultMeta1, ChangeControlStatus changeControlStatus);

    boolean existsByResultMeta2AndStatus(String resultMeta2, ChangeControlStatus changeControlStatus);

}
