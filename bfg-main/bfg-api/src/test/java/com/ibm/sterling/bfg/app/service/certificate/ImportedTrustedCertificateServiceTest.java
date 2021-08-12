package com.ibm.sterling.bfg.app.service.certificate;

import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class ImportedTrustedCertificateServiceTest {

    @InjectMocks
    private ChangeControlCertService changeControlCertService;

    @InjectMocks
    private TrustedCertificateDetailsService trustedCertificateDetailsService;

    @InjectMocks
    private CertificateIntegrationService certificateIntegrationService;

    @InjectMocks
    private TrustedCertificateService trustedCertificateService;

    @Mock
    private TrustedCertificateRepository trustedCertificateRepository;

    @InjectMocks
    private AdminAuditService adminAuditService;

    @Test
    void importCertificatesFromB2B() {

    }
}