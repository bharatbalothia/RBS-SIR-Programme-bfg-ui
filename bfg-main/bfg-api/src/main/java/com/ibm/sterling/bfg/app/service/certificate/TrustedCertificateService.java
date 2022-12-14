package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.certificate.CertType;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.naming.InvalidNameException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface TrustedCertificateService extends FieldValueExists {
    List<TrustedCertificate> listAll();

    TrustedCertificate getTrustedCertificateById(String id);

    TrustedCertificate convertX509CertificateToTrustedCertificate(X509Certificate x509Certificate, String certName, String comment)
            throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException;

    TrustedCertificate saveCertificateToChangeControl(TrustedCertificate cert, Operation operation) throws CertificateException;

    TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControl, String approverComments, ChangeControlStatus status)
            throws JsonProcessingException, java.security.cert.CertificateEncodingException;

    Page<CertType> findCertificates(Pageable pageable, String certName, String thumbprint);

    TrustedCertificateDetails findCertificateDataById(String id) throws JsonProcessingException, InvalidNameException,
            NoSuchAlgorithmException, java.security.cert.CertificateEncodingException;

    TrustedCertificateDetails findPendingCertificateDataById(String id) throws JsonProcessingException, InvalidNameException,
            NoSuchAlgorithmException, java.security.cert.CertificateEncodingException;

    String existsByNameInDbAndBI(String name) throws JsonProcessingException;

    TrustedCertificate updatePendingCertificate(ChangeControlCert changeControl, String certName, String changerComments);

    void cancelPendingCertificate(ChangeControlCert changeControl);

    TrustedCertificate save(TrustedCertificate trustedCertificate);
}
