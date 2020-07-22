package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.FileTypeNotValidException;
import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.certificate.CertificateValidationService;
import com.ibm.sterling.bfg.app.service.certificate.ChangeControlCertService;
import com.ibm.sterling.bfg.app.service.certificate.TrustedCertificateService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

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
    private ChangeControlCertService changeControlCertService;

    @Autowired
    private TrustedCertificateRepository trustedCertificateRepository;

    @Autowired
    private ChangeControlCertRepository changeControlCertRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public Page<CertType> getCertificates(@RequestParam(value = "certName", defaultValue = "", required = false) String certName,
                                          @RequestParam(value = "thumbprint", defaultValue = "", required = false) String thumbprint,
                                          @RequestParam(value = "thumbprint256", defaultValue = "", required = false) String thumbprint256,
                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                          @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return certificateService.findCertificates(PageRequest.of(page, size), certName, thumbprint, thumbprint256);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificateDetails> uploadFile(@RequestParam("file") MultipartFile certificate)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException {
        if (!"application/x-x509-ca-cert".equals(certificate.getContentType()))
            throw new FileTypeNotValidException();
        return ok(new TrustedCertificateDetails(getX509Certificate(certificate), certificateValidationService,
                trustedCertificateRepository, changeControlCertRepository, false));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificate> createTrustedCertificate(@RequestParam("file") MultipartFile certificate,
                                                                       @RequestParam String name,
                                                                       @RequestParam String comments)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException, CertificateEncodingException {
        return ok(certificateService.saveCertificateToChangeControl(
                certificateService.convertX509CertificateToTrustedCertificate(getX509Certificate(certificate), name, comments), CREATE));
    }

    private X509Certificate getX509Certificate(MultipartFile certificate) throws CertificateException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(certificate.getInputStream());
    }

    @PostMapping("pending")
    public ResponseEntity<TrustedCertificate> postPendingCertificates(@RequestBody Map<String, Object> approve) throws Exception {
        ChangeControlStatus status = ChangeControlStatus.valueOf((String) approve.get("status"));
        String changeId = (String) approve.get("changeID");
        return Optional.ofNullable(
                certificateService.getTrustedCertificateAfterApprove(
                        changeControlCertService.findById(changeId).orElseThrow(CertificateNotFoundException::new),
                        (String) approve.get("approverComments"),
                        status))
                .map(record -> ok()
                        .body(record))
                .orElseThrow(CertificateNotFoundException::new);
    }

    @GetMapping("pending")
    public Page<ChangeControlCert> getPendingCertificates(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                          @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return ListToPageConverter.convertListToPage(
                new ArrayList<>(changeControlCertService.findAllPending()), PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public ResponseEntity<TrustedCertificateDetails> getCertificateById(@PathVariable(name = "id") String id) throws JsonProcessingException,
            NoSuchAlgorithmException, InvalidNameException, java.security.cert.CertificateEncodingException {
        return ok().body(certificateService.findById(id));
    }

}
