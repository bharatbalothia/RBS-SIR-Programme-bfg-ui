package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.certificate.FileNotValidException;
import com.ibm.sterling.bfg.app.model.certificate.*;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.service.certificate.CertificateValidationService;
import com.ibm.sterling.bfg.app.service.certificate.ChangeControlCertService;
import com.ibm.sterling.bfg.app.service.certificate.TrustedCertificateDetailsService;
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
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.ibm.sterling.bfg.app.model.changecontrol.Operation.CREATE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/certificates")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
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

    @Autowired
    private TrustedCertificateDetailsService trustedCertificateDetailsService;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

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
    public ResponseEntity<TrustedCertificateDetails> uploadFile(@RequestParam("file") MultipartFile file)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException {
        return ok(trustedCertificateDetailsService.getTrustedCertificateDetails(getX509Certificate(file), false));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificate> createTrustedCertificate(@RequestParam("file") MultipartFile file,
                                                                       @RequestParam String name,
                                                                       @RequestParam String comments)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException, CertificateEncodingException {
        return ok(certificateService.saveCertificateToChangeControl(
                certificateService.convertX509CertificateToTrustedCertificate(getX509Certificate(file), name, comments), CREATE));
    }

    private X509Certificate getX509Certificate(MultipartFile file) throws CertificateException, IOException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Certificate;
        try (InputStream inputStream = file.getInputStream()) {
            x509Certificate = (X509Certificate) factory.generateCertificate(inputStream);
        } catch (CertificateException e) {
            throw new FileNotValidException();
        }
        return x509Certificate;
    }

    @PostMapping("pending")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_APPROVE')")
    public ResponseEntity<TrustedCertificate> postPendingCertificates(@RequestBody Map<String, Object> approve) throws Exception {
        ChangeControlCert changeControlCert = changeControlCertService.getChangeControlCertById(String.valueOf(approve.get("changeID")));
        return Optional.ofNullable(certificateService.getTrustedCertificateAfterApprove(
                changeControlCert,
                String.valueOf(approve.get("approverComments")),
                ChangeControlStatus.valueOf(String.valueOf(approve.get("status"))))
        ).map(record -> ok()
                .body(record))
                .orElseThrow(CertificateNotFoundException::new);
    }

    @GetMapping("pending/{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public ResponseEntity<ChangeControlCert> getPendingCertificates(@PathVariable(name = "id") String id) {
        return ok(changeControlCertService.getChangeControlCertById(id));
    }

    @GetMapping("pending")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public Page<ChangeControlCert> getPendingCertificate(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return ListToPageConverter.convertListToPage(
                new ArrayList<>(changeControlCertService.findAllPending()), PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public ResponseEntity<TrustedCertificate> getCertificateById(@PathVariable(name = "id") String id) {
        return ok(certificateService.getTrustedCertificateById(id));
    }

    @PutMapping("pending/{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificate> editPendingCertificates(@PathVariable(name = "id") String id,
                                                                      @RequestBody Map<String, Object> edit) {
        ChangeControlCert changeControlCert = changeControlCertService.getChangeControlCertById(id);
        apiDetailsHandler.checkPermissionForUpdateChangeControl(changeControlCert.getChanger());
        String name = String.valueOf(edit.get("name"));
        String comments = String.valueOf(edit.get("comments"));
        return ok(certificateService.editChangeControl(changeControlCert, name, comments));
    }

    @DeleteMapping("pending/{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<ChangeControlCert> deletePendingCertificates(@PathVariable(name = "id") String id) {
        ChangeControlCert changeControlCert = changeControlCertService.getChangeControlCertById(id);
        apiDetailsHandler.checkPermissionForUpdateChangeControl(changeControlCert.getChanger());
        certificateService.deleteChangeControl(changeControlCert);
        return ok(changeControlCert);
    }

    @GetMapping("/validate/{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public ResponseEntity<TrustedCertificateDetails> validateCertificateById(@PathVariable(name = "id") String id) throws JsonProcessingException,
            NoSuchAlgorithmException, InvalidNameException, java.security.cert.CertificateEncodingException {
        return ok(certificateService.findCertificateDataById(id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_DELETE')")
    public ResponseEntity<?> deleteTrustedCertificate(@PathVariable String id, @RequestParam(required = false) String changerComments)
            throws CertificateException {
        TrustedCertificate cert = certificateService.getTrustedCertificateById(id);
        Optional.ofNullable(changerComments).ifPresent(cert::setChangerComments);
        return ok(certificateService.saveCertificateToChangeControl(cert, Operation.DELETE));
    }

    @GetMapping("/existence")
    public ResponseEntity<?> isExistingCertificateName(@RequestParam String name) throws JsonProcessingException {
        return ok(certificateService.existsByNameInDbAndBI(name));
    }

}
