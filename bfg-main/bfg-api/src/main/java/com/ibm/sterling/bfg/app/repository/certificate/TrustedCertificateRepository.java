package com.ibm.sterling.bfg.app.repository.certificate;

import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrustedCertificateRepository extends JpaRepository<TrustedCertificate, String> {
    List<TrustedCertificate> findAll(Specification<TrustedCertificate> specification);

    boolean existsByThumbprint(String thumbprint);

    boolean existsByThumbprint256(String thumbprint256);
}
