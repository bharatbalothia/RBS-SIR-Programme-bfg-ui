package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.InvalidNameException;
import javax.security.cert.CertificateEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface TrustedCertificateService extends FieldValueExists {
    List<TrustedCertificate> listAll();

    TrustedCertificateDetails findById(String id) throws JsonProcessingException, InvalidNameException, NoSuchAlgorithmException, java.security.cert.CertificateEncodingException;

    TrustedCertificate convertX509CertificateToTrustedCertificate(X509Certificate x509Certificate,
                                                                  String certName,
                                                                  String comment) throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException, CertificateEncodingException;

    TrustedCertificate saveCertificateToChangeControl(TrustedCertificate cert, Operation operation) throws CertificateException;

    TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControl, String approverComments, ChangeControlStatus status) throws Exception;

    Page<CertType> findCertificates(Pageable pageable, String certName, String thumbprint, String thumbprint256);

    TrustedCertificateDetails findCertificateDataById(String id) throws JsonProcessingException, InvalidNameException,
            NoSuchAlgorithmException, java.security.cert.CertificateEncodingException;

}
