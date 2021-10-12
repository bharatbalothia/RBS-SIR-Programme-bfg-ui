package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.IntegratedCertificateData;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.InvalidNameException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportedTrustedCertificateServiceTest {
    @InjectMocks
    private ImportedTrustedCertificateService importedService;

    @Mock
    private ChangeControlCertService changeControlCertService;

    @Mock
    private TrustedCertificateDetailsService trustedCertificateDetailsService;

    @Mock
    private CertificateIntegrationService certificateIntegrationService;

    @Mock
    private TrustedCertificateService trustedCertificateService;

    @Mock
    private TrustedCertificateRepository trustedCertificateRepository;

    @Mock
    private AdminAuditService adminAuditService;

    private IntegratedCertificateData importedCert;
    private TrustedCertificateDetails certificateDetails;
    private TrustedCertificate trustedCertificate;
    private ChangeControlCert controlCert;

    @BeforeEach
    public void setUp() {
        importedCert = new IntegratedCertificateData();
        importedCert.set_id("TEST certificate");
        importedCert.set_title("TrustedDigitalCertificate(TEST certificate)");
        importedCert.setHref("http://test.com:25074/trusteddigitalcertificates/TEST+certificate");
        importedCert.setCertNameAndDate(
                new IntegratedCertNameAndDate("TEST certificate", LocalDateTime.now()));
        importedCert.setCertData("MIIC7TCCAdWgAwIBAgIBATANBgkqhkiG9w0BAQsFADAmMSQwIgYDVQQDDBtpbmdyZXNzLW9wZXJhdG9yQDE2MTMwMzkxODAwHhcNMjEwMjExMTAyNjE5WhcNMjMwMjExMTAyNjIwWjAmMSQwIgYDVQQDDBtpbmdyZXNzLW9wZXJhdG9yQDE2MTMwMzkxODAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCvcZ0Oc9AWFJldWGn17rXnIn/YaBWsZ1CXihFOYver+ofPFpEM98RWaBMKbI3cLvir4ry4hHY3p8K9fSsrHFB+gLmW08MowMzxR36t4r7z/d5iD9PqycLWGpHTC8k10n/JGKKeku4e7zosMLIeHQh9NFp+U/OcbbvZb3BhlOj7Vtbo+C4odl4DTFVNbyxZ5+CjFTVrJIfb/VFrCWUfaenomntyiZ8qsso+A6hHj9ZrUtMK1czBZWPc5tq9v853whBBoWrAdoSsnsorRSjTPNoZkqUgdE/gA4wad4fMNbXCy/F2m+pf/Gmi1i+X69hQr/n4LVIUN+JaFxRyYcNl0l4lAgMBAAGjJjAkMA4GA1UdDwEB/wQEAwICpDASBgNVHRMBAf8ECDAGAQH/AgEAMA0GCSqGSIb3DQEBCwUAA4IBAQA2mYpB1eyQmvN0s4F08Idp7ARU94N6IOG03PJTF/u6eSOm2z/7ETfNsCAVY+KDqgCZ6YDPbTiLO2KICn4SwHOTku5yqNcRwcJndebxYF5NGT+jYjtkVJAa+g38Qx/zYFZGHdUm0ZmT+/rrdINpqQUsSu+xCHA311UMdXouqMd4W942hoWEYHOtbEL5H8LfmdEwVeudy1WoDAmnl2S4WLn83BVSAXyx9a5hsMLWmPSqcQhZn4EiWbmph2spRzU3G9DJa3HD75Xm5//53RajkCLEIU28G9Efq3K59xjXSbNpuJB0P0whDoWlsPKLnJ7bQIjBJ/eiTsTjx5pyuoX7dnOn");
        trustedCertificate = new TrustedCertificate();
        trustedCertificate.setCertificateName("TEST certificate");
        trustedCertificate.setThumbprint("Sha-1");
        trustedCertificate.setThumbprint256("Sha-2");
        controlCert = new ChangeControlCert();
        certificateDetails = new TrustedCertificateDetails();
        certificateDetails.setThumbprint("Sha-1");
        certificateDetails.setThumbprint256("Sha-2");
    }

    @Test
    void importCertificatesFromB2B_ShouldDeleteTrustedCertificate() throws JsonProcessingException, InvalidNameException, NoSuchAlgorithmException, CertificateEncodingException {

        when(trustedCertificateService.listAll()).thenReturn(Collections.singletonList(new TrustedCertificate()));
        when(certificateIntegrationService.getCertificates()).thenReturn(Collections.emptyList());
        when(changeControlCertService.save(any(ChangeControlCert.class))).thenReturn(controlCert);
        doNothing().when(adminAuditService).fireAdminAuditEvent(any(AdminAuditEventRequest.class));
        importedService.importCertificatesFromB2B();
        verify(trustedCertificateService, times(1)).listAll();
    }
}

