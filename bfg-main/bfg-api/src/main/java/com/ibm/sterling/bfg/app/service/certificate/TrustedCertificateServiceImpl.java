package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotValidException;
import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForApprovalException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.certificate.*;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateLogRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import com.ibm.sterling.bfg.app.service.GenericSpecification;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
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
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.ACCEPTED;
import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.PENDING;
import static com.ibm.sterling.bfg.app.model.changecontrol.Operation.DELETE;

@Service
public class TrustedCertificateServiceImpl implements TrustedCertificateService {

    private static final Logger LOG = LogManager.getLogger(TrustedCertificateServiceImpl.class);
    private static final String CERTIFICATE_FILE_MISSING = "The certificate file is missing";
    private static final String NO_CERTIFICATE_DATA = "There is no certificate data";

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
    private CertificateIntegrationService certificateIntegrationService;

    @Autowired
    private TrustedCertificateDetailsService trustedCertificateDetailsService;

    @Autowired
    private AdminAuditService adminAuditService;

    @Autowired
    private Validator validator;

    @Override
    public List<TrustedCertificate> listAll() {
        return trustedCertificateRepository.findAll();
    }

    @Override
    public TrustedCertificate getTrustedCertificateById(String id) {
        LOG.info("Trusted certificate by id {}", id);
        return trustedCertificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException(NO_CERTIFICATE_DATA + " in SCT_TRUSTED_CERTIFICATE"));
    }

    @Override
    public TrustedCertificateDetails findCertificateDataById(String id)
            throws JsonProcessingException, InvalidNameException, NoSuchAlgorithmException, CertificateEncodingException {
        X509Certificate x509Certificate;
        Optional<TrustedCertificateLog> trustedCertificateLog = trustedCertificateLogRepository.findById(id);
        if (trustedCertificateLog.isPresent()) {
            x509Certificate = Optional.ofNullable(trustedCertificateLog.get().getCertificate())
                    .orElseThrow(() -> new CertificateNotFoundException(CERTIFICATE_FILE_MISSING + " in SCT_TRUSTED_CERTIFICATE_LOG"));
        } else {
            x509Certificate = trustedCertificateRepository.findById(id)
                    .map(trustedCertificate -> Optional.ofNullable(trustedCertificate.getCertificate())
                            .orElseThrow(() -> new CertificateNotFoundException(CERTIFICATE_FILE_MISSING + " in SCT_TRUSTED_CERTIFICATE")))
                    .orElseThrow(() -> new CertificateNotFoundException(
                            NO_CERTIFICATE_DATA + " in SCT_TRUSTED_CERTIFICATE_LOG and SCT_TRUSTED_CERTIFICATE by " + id)
                    );
        }
        return trustedCertificateDetailsService.getTrustedCertificateDetails(x509Certificate, true);
    }

    @Override
    public TrustedCertificate editChangeControl(ChangeControlCert changeControlCert, String certName, String changerComments) {
        checkStatusOfChangeControl(changeControlCert);
        if (!changeControlCert.getOperation().equals(Operation.DELETE)) {
            TrustedCertificateLog trustedCertificateLog = changeControlCert.getTrustedCertificateLog();
            Optional.ofNullable(certName).ifPresent(name -> {
                trustedCertificateLog.setCertificateName(name);
                changeControlCert.setResultMeta1(name);
            });
            TrustedCertificate trustedCertificate = changeControlCert.convertTrustedCertificateLogToTrustedCertificate();
            validateCertificate(trustedCertificate);

            Optional.ofNullable(changerComments).ifPresent(comments -> {
                changeControlCert.setChangerComments(comments);
                trustedCertificate.setChangerComments(changerComments);
            });
            changeControlCertService.save(changeControlCert);
            return trustedCertificate;
        }
        return null;
    }

    @Override
    public Boolean existsByNameInDbAndBI(String name) throws JsonProcessingException {
        LOG.info("Trusted certificate exists by {} name", name);
        return trustedCertificateRepository.existsByCertificateName(name) ||
                Optional.ofNullable(certificateIntegrationService.getCertificateByName(name)).isPresent();
    }

    @Override
    public void deleteChangeControl(ChangeControlCert changeControlCert) {
        LOG.info("Deleting pending {}", changeControlCert);
        checkStatusOfChangeControl(changeControlCert);
        changeControlCertRepository.delete(changeControlCert);
    }

    private void checkStatusOfChangeControl(ChangeControlCert changeControlCert) {
        if (!PENDING.equals(changeControlCert.getStatus())) {
            throw new StatusNotPendingException();
        }
    }

    public TrustedCertificate convertX509CertificateToTrustedCertificate(X509Certificate x509Certificate,
                                                                         String certificateName, String comment)
            throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException {
        TrustedCertificateDetails trustedCertificateDetails =
                trustedCertificateDetailsService.getTrustedCertificateDetails(x509Certificate, false);
        if (!trustedCertificateDetails.getIsValid())
            throw new CertificateNotValidException();
        TrustedCertificate trustedCertificate = trustedCertificateDetails.convertToTrustedCertificate();
        trustedCertificate.setCertificateName(certificateName);
        trustedCertificate.setChangerComments(comment);
        trustedCertificate.setCertificate(x509Certificate);
        return trustedCertificate;
    }

    private void validateCertificate(TrustedCertificate trustedCertificate) {
        Set<ConstraintViolation<TrustedCertificate>> violations = validator.validate(trustedCertificate);
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
        adminAuditService.fireAdminAuditEvent(new AdminAuditEventRequest(changeControlCert, changeControlCert.getChanger()));
        return cert;
    }

    @Override
    public TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControlCert,
                                                                String approverComments, ChangeControlStatus status)
            throws JsonProcessingException, CertificateEncodingException {
        checkStatusOfChangeControl(changeControlCert);
        TrustedCertificate cert = new TrustedCertificate();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ACCEPTED.equals(status)) {
            if (userName.equals(changeControlCert.getChanger()))
                throw new InvalidUserForApprovalException();
            cert = approveCertificate(changeControlCert);
        }
        changeControlCertService.setApproveInfo(changeControlCert, userName, approverComments, status);
        adminAuditService.fireAdminAuditEvent(new AdminAuditEventRequest(changeControlCert, changeControlCert.getApprover()));
        return cert;
    }

    private TrustedCertificate approveCertificate(ChangeControlCert changeControlCert)
            throws CertificateEncodingException, JsonProcessingException {
        LOG.info("Approve the Trusted certificate {} action", changeControlCert.getOperation());
        TrustedCertificate trustedCertificate = changeControlCert.convertTrustedCertificateLogToTrustedCertificate();
        Operation operation = changeControlCert.getOperation();
        if (operation.equals(DELETE)) {
            certificateIntegrationService.deleteCertificateByName(trustedCertificate.getCertificateName());
            trustedCertificateRepository.delete(trustedCertificate);
        } else {
            validateCertificate(trustedCertificate);
            CertificateDataIntegrationRequest certificateDataIntegrationRequest = new CertificateDataIntegrationRequest(
                    DatatypeConverter.printBase64Binary(trustedCertificate.getCertificate().getEncoded()),
                    trustedCertificate.getCertificateName(),
                    true,
                    true
            );
            certificateIntegrationService.createCertificate(certificateDataIntegrationRequest);
            trustedCertificateRepository.save(trustedCertificate);
        }
        TrustedCertificateLog certLog = changeControlCert.getTrustedCertificateLog();
        certLog.setCertificateId(trustedCertificate.getCertificateId());
        changeControlCert.setTrustedCertificateLog(certLog);
        changeControlCertService.save(changeControlCert);
        return trustedCertificate;
    }

    @Override
    public Page<CertType> findCertificates(Pageable pageable, String certName, String thumbprint, String thumbprint256) {
        LOG.info("Search trusted certificates by trusted certificate name {}, thumbprint {} and thumbprint256 {}",
                certName, thumbprint, thumbprint256);
        List<CertType> certificates = new ArrayList<>();
        Specification<TrustedCertificate> specification = Specification
                .where(
                        GenericSpecification.<TrustedCertificate>filter(certName, "certificateName"))
                .and(
                        GenericSpecification.filter(thumbprint, "thumbprint"))
                .and(
                        GenericSpecification.filter(thumbprint256, "thumbprint256")
                );
        List<TrustedCertificate> certificateList = trustedCertificateRepository.findAll(specification);
        List<ChangeControlCert> ccList = changeControlCertService.findAllPending(certName, thumbprint, thumbprint256);
        certificateList.removeIf(cert ->
                ccList.stream().anyMatch(control -> control.getResultMeta3().equals(cert.getThumbprint256())));
        certificates.addAll(certificateList);
        certificates.addAll(ccList);
        certificates.sort(Comparator.comparing(CertType::nameForSorting, String.CASE_INSENSITIVE_ORDER));
        return ListToPageConverter.convertListToPage(certificates, pageable);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (fieldName.equals("CERTIFICATE_NAME"))
            return trustedCertificateRepository.existsByCertificateName(String.valueOf(value));
        return false;
    }

}
