package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.model.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.service.CertificateValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

    @Autowired
    private CertificateValidationService certificateValidationService;

    @PostMapping("/upload")
    public ResponseEntity<TrustedCertificateDetails> uploadFile(@RequestParam("file") MultipartFile certificate)
            throws CertificateException, IOException, InvalidNameException, NoSuchAlgorithmException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Certificate = (X509Certificate) factory.generateCertificate(certificate.getInputStream());
        return ok(new TrustedCertificateDetails(x509Certificate, certificateValidationService));
    }

}
