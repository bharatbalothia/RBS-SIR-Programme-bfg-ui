package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.model.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.service.CertificateValidationService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

    @Autowired
    private CertificateValidationService certificateValidationService;

    @GetMapping
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS')")
    public Page<?> getCertificates(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                   @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return ListToPageConverter.convertListToPage(new ArrayList<String>(), PageRequest.of(page, size));
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('FB_UI_TRUSTED_CERTS_NEW')")
    public ResponseEntity<TrustedCertificateDetails> uploadFile(@RequestParam("file") MultipartFile certificate)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Certificate = (X509Certificate) factory.generateCertificate(certificate.getInputStream());
        return ok(new TrustedCertificateDetails(x509Certificate, certificateValidationService));
    }

}
