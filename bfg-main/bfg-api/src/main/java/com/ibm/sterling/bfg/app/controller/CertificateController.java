package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.service.certificate.CertificateValidationService;
import com.ibm.sterling.bfg.app.service.certificate.ChangeControlCertService;
import com.ibm.sterling.bfg.app.service.certificate.TrustedCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.InvalidNameException;
import javax.security.cert.CertificateEncodingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static com.ibm.sterling.bfg.app.model.changeControl.Operation.CREATE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

    @Autowired
    private CertificateValidationService certificateValidationService;

    @Autowired
    private TrustedCertificateService certificateService;

    @Autowired
    private ChangeControlCertService changeControlService;

    @GetMapping
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public Page<CertType> getCertificates(@RequestParam(value = "cert-name", defaultValue = "", required = false) String certName,
                                          @RequestParam(value = "thumbprint", defaultValue = "", required = false) String thumbprint,
                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                          @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return certificateService.findCertificates(PageRequest.of(page, size), certName, thumbprint);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificateDetails> uploadFile(@RequestParam("file") MultipartFile certificate)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException {
        return ok(new TrustedCertificateDetails(getX509Certificate(certificate), certificateValidationService));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificate> createTrustedCertificate(@RequestParam("file") MultipartFile certificate,
                                                                       @RequestParam String name,
                                                                       @RequestParam String comments)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException, CertificateEncodingException {
        return ok(certificateService.saveCertificateToChangeControl(
                certificateService.convertX509CertificateToTrustedCertificate(getX509Certificate(certificate), name, comments), CREATE)
        );
    }

    private X509Certificate getX509Certificate(MultipartFile certificate) throws CertificateException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(certificate.getInputStream());
    }

}
