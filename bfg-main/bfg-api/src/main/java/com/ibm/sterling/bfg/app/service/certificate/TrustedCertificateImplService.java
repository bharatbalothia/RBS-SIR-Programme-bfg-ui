package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.CertificateNotValidException;
import com.ibm.sterling.bfg.app.exception.StatusNotPendingException;
import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateLog;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateLogRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.GenericSpecification;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus.ACCEPTED;
import static com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus.PENDING;
import static com.ibm.sterling.bfg.app.model.changeControl.Operation.DELETE;

@Service
public class TrustedCertificateImplService implements TrustedCertificateService {
    private static final Logger LOG = LogManager.getLogger(TrustedCertificateImplService.class);

    @Autowired
    private TrustedCertificateRepository trustedCertificateRepository;

    @Autowired
    private TrustedCertificateLogRepository trustedCertificateLogRepository;

    @Autowired
    private ChangeControlCertService changeControlCertService;

    @Autowired
    private CertificateValidationService certificateValidationService;

    @Autowired
    private ChangeControlCertRepository changeControlCertRepository;

    @Autowired
    private Validator validator;

    @Override
    public List<TrustedCertificate> listAll() {
        return trustedCertificateRepository.findAll();
    }

    @Override
    public TrustedCertificate findById(String id) {
        LOG.info("Trusted certificate by id {}", id);
        return trustedCertificateRepository.findById(id)
                .orElseThrow(CertificateNotFoundException::new);
    }

    @Override
    public TrustedCertificateDetails findCertificateDataById(String id) throws JsonProcessingException, InvalidNameException,
            NoSuchAlgorithmException, CertificateEncodingException {
        Optional<TrustedCertificate> trustedCertificate = trustedCertificateRepository.findById(id);
        Optional<TrustedCertificateLog> trustedCertificateLog = trustedCertificateLogRepository.findById(id);
        X509Certificate certificate;
        if (trustedCertificate.isPresent()) {
            certificate = trustedCertificate.get().getCertificate();
        } else {
            certificate = trustedCertificateLog.map(TrustedCertificateLog::getCertificate).orElseThrow(CertificateNotFoundException::new);
        }
        return new TrustedCertificateDetails(certificate,
                certificateValidationService, trustedCertificateRepository, changeControlCertRepository, true);
    }

    public TrustedCertificate convertX509CertificateToTrustedCertificate(X509Certificate x509Certificate,
                                                                         String certName,
                                                                         String comment)
            throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException {
        TrustedCertificateDetails trustedCertificateDetails = new TrustedCertificateDetails(x509Certificate,
                certificateValidationService, trustedCertificateRepository, changeControlCertRepository, false);
        if (!trustedCertificateDetails.isValid())
            throw new CertificateNotValidException();
        TrustedCertificate trustedCertificate = trustedCertificateDetails.convertToTrustedCertificate();
        trustedCertificate.setCertificateName(certName);
        trustedCertificate.setChangerComments(comment);
        trustedCertificate.setCertificate(x509Certificate);
        return trustedCertificate;
    }

    private void validateCertificate(TrustedCertificate trustedCertificate) {
        Set<ConstraintViolation<TrustedCertificate>> violations;
        violations = validator.validate(trustedCertificate);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public TrustedCertificate saveCertificateToChangeControl(TrustedCertificate cert, Operation operation) {
        if (!operation.equals(Operation.DELETE)) {
            validateCertificate(cert);
        }
        LOG.info("Trying to save trusted certificate {} to change control", cert);
        ChangeControlCert changeControlCert = new ChangeControlCert();
        changeControlCert.setOperation(operation);
        changeControlCert.setChanger(SecurityContextHolder.getContext().getAuthentication().getName());
        changeControlCert.setChangerComments(cert.getChangerComments());
        changeControlCert.setResultMeta1(cert.getCertificateName());
        changeControlCert.setResultMeta2(cert.getThumbprint());
        changeControlCert.setResultMeta3(cert.getThumbprint256());
        changeControlCert.setTrustedCertificateLog(new TrustedCertificateLog(cert));
        cert.setChangeID(changeControlCertService.save(changeControlCert).getChangeID());
        return cert;
    }

    @Override
    public TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControlCert,
                                                                String approverComments, ChangeControlStatus status) throws Exception {
        if (changeControlCert.getStatus() != PENDING) {
            throw new StatusNotPendingException();
        }
        TrustedCertificate cert = new TrustedCertificate();
        if (ACCEPTED.equals(status))
            cert = saveTrustedCertificateAfterApprove(changeControlCert);
        changeControlCertService.setApproveInfo(
                changeControlCert,
                SecurityContextHolder.getContext().getAuthentication().getName(),
                approverComments,
                status
        );
        return cert;
    }

    private TrustedCertificate saveTrustedCertificateAfterApprove(ChangeControlCert changeControlCert) {
        LOG.info("Approve the Trusted certificate {} action", changeControlCert.getOperation());
        TrustedCertificate cert = changeControlCert.convertTrustedCertificateLogToTrustedCertificate();
        Operation operation = changeControlCert.getOperation();
        if (operation.equals(DELETE)) {
            trustedCertificateRepository.delete(cert);
        } else {
            validateCertificate(cert);
            trustedCertificateRepository.save(cert);
        }
        TrustedCertificateLog certLog = changeControlCert.getTrustedCertificateLog();
        certLog.setCertificateId(cert.getCertificateId());
        changeControlCert.setTrustedCertificateLog(certLog);
        changeControlCertService.save(changeControlCert);
        return cert;
    }

    @Override
    public Page<CertType> findCertificates(Pageable pageable, String certName, String thumbprint, String thumbprint256) {
        LOG.info("Search trusted certificates by trusted certificate name {}, thumbprint {} and thumbprint256 {}", certName, thumbprint, thumbprint256);
        List<CertType> certificates = new ArrayList<>(changeControlCertService.findAllPending(certName, thumbprint, thumbprint256));
        Specification<TrustedCertificate> specification = Specification
                .where(
                        GenericSpecification.<TrustedCertificate>filter(certName, "certificateName"))
                .and(
                        GenericSpecification.filter(thumbprint, "thumbprint"))
                .and(
                        GenericSpecification.filter(thumbprint256, "thumbprint256"));
        certificates.addAll(
                trustedCertificateRepository
                        .findAll(specification));
        certificates.sort(Comparator.comparing(o -> o.nameForSorting().toLowerCase()));
        return ListToPageConverter.convertListToPage(certificates, pageable);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (fieldName.equals("CERTIFICATE_NAME"))
            return trustedCertificateRepository.existsByCertificateName(String.valueOf(value));
        return false;
    }

}
