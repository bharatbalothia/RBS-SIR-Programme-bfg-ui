package com.ibm.sterling.bfg.app.repository.certificate;

import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TrustedCertificateLogRepository extends JpaRepository<TrustedCertificateLog, String> {
}
