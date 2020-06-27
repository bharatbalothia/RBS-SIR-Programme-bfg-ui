package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.InvalidNameException;
import javax.security.cert.CertificateEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface TrustedCertificateService {
    List<TrustedCertificate> listAll();

    TrustedCertificate findById(String id) throws JsonProcessingException;

    TrustedCertificate convertX509CertificateToTrustedCertificate(X509Certificate x509Certificate,
                                                                  String certName,
                                                                  String comment) throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException, CertificateEncodingException;

    TrustedCertificate saveCertificateToChangeControl(TrustedCertificate cert, Operation operation) throws CertificateException;

    TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControl, String approverComments, ChangeControlStatus status) throws Exception;

    Page<CertType> findEntities(Pageable pageable, String certName, String thumbprint);
}
