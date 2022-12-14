package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotValidException;
import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForApprovalException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.audit.EventType;
import com.ibm.sterling.bfg.app.model.certificate.*;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.validation.CertificateValidationComponent;
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
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InvalidNameException;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.ACCEPTED;
import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.PENDING;
import static com.ibm.sterling.bfg.app.model.changecontrol.Operation.DELETE;

@Service
@Transactional(readOnly = true)
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
    private CertificateValidationComponent certificateValidation;

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
        X509Certificate x509Certificate = trustedCertificateRepository.findById(id)
                .map(TrustedCertificate::getCertificate)
                .orElseThrow(() -> new CertificateNotFoundException(CERTIFICATE_FILE_MISSING + " in SCT_TRUSTED_CERTIFICATE"));
        return trustedCertificateDetailsService.getTrustedCertificateDetails(x509Certificate, false);
    }

    @Override
    public TrustedCertificateDetails findPendingCertificateDataById(String id)
            throws JsonProcessingException, InvalidNameException, NoSuchAlgorithmException, CertificateEncodingException {
        X509Certificate x509Certificate = trustedCertificateLogRepository.findById(id)
                .map(TrustedCertificateLog::getCertificate)
                .orElseThrow(() -> new CertificateNotFoundException(CERTIFICATE_FILE_MISSING + " in SCT_TRUSTED_CERTIFICATE_LOG"));
        return trustedCertificateDetailsService.getTrustedCertificateDetails(x509Certificate, false);
    }

    @Override
    public String existsByNameInDbAndBI(String name) throws JsonProcessingException {
        LOG.info("Trusted certificate exists by {} name", name);
        List<String> existence = new ArrayList<>();
        if (Optional.ofNullable(certificateIntegrationService.getCertificateByName(name)).isPresent())
            existence.add("the Sterling B2B Integrator");
        if (trustedCertificateRepository.existsByCertificateName(name))
            existence.add("the BFGUI");
        if (existence.isEmpty()) return null;
        return "The Trusted Certificate with this name already exists in " + String.join(" and ", existence);
    }

    @Override
    @Transactional
    public TrustedCertificate updatePendingCertificate(ChangeControlCert changeControl, String certName, String changerComments) {
        String currentName = changeControl.getResultMeta1();
        String actionValue = currentName.equals(certName) ? currentName : currentName + " -> " + certName;
        TrustedCertificate trustedCertificate = changeControlCertService.updateChangeControlCert(changeControl, certName, changerComments);
        adminAuditService.fireAdminAuditEvent(
                new AdminAuditEventRequest(changeControl, EventType.REQUEST_EDITED, actionValue));
        return trustedCertificate;
    }

    @Override
    @Transactional
    public void cancelPendingCertificate(ChangeControlCert changeControl) {
        changeControlCertService.deleteChangeControl(changeControl);
        adminAuditService.fireAdminAuditEvent(
                new AdminAuditEventRequest(changeControl, EventType.REQUEST_CANCELLED, changeControl.getResultMeta1()));
    }

    @Transactional
    public TrustedCertificate save(TrustedCertificate trustedCertificate) {
        LOG.info("Persisting {} to SCT_TRUSTED_CERTIFICATE", trustedCertificate);
        return trustedCertificateRepository.save(trustedCertificate);
    }

    public TrustedCertificate convertX509CertificateToTrustedCertificate(
            X509Certificate x509Certificate, String certificateName, String comment)
            throws CertificateException, InvalidNameException, NoSuchAlgorithmException, JsonProcessingException {
        TrustedCertificateDetails trustedCertificateDetails =
                trustedCertificateDetailsService.getTrustedCertificateDetails(x509Certificate, true);
        if (!trustedCertificateDetails.isValid())
            throw new CertificateNotValidException("Trusted certificate " + certificateName + " is not valid");
        TrustedCertificate trustedCertificate = trustedCertificateDetails.convertToTrustedCertificate();
        trustedCertificate.setCertificateName(certificateName);
        trustedCertificate.setChangerComments(comment);
        trustedCertificate.setCertificate(x509Certificate);
        return trustedCertificate;
    }

    @Override
    @Transactional
    public TrustedCertificate saveCertificateToChangeControl(TrustedCertificate cert, Operation operation) {
        changeControlCertService.checkOnPendingState(cert.getCertificateName());
        if (!operation.equals(Operation.DELETE)) {
            certificateValidation.validateCertificate(cert);
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
    @Transactional
    public TrustedCertificate getTrustedCertificateAfterApprove(ChangeControlCert changeControlCert,
                                                                String approverComments, ChangeControlStatus status)
            throws JsonProcessingException, CertificateEncodingException {
        if (!PENDING.equals(changeControlCert.getStatus())) {
            throw new StatusNotPendingException("Status of CC " + changeControlCert.getChangeID() +
                    " is not pending");
        }
        TrustedCertificate cert = new TrustedCertificate();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ACCEPTED.equals(status)) {
            if (userName.equals(changeControlCert.getChanger()))
                throw new InvalidUserForApprovalException(userName + " is invalid for APPROVAL");
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
            certificateValidation.validateCertificate(trustedCertificate);
            CertificateDataIntegrationRequest certificateDataIntegrationRequest = new CertificateDataIntegrationRequest(
                    DatatypeConverter.printBase64Binary(trustedCertificate.getCertificate().getEncoded()),
                    trustedCertificate.getCertificateName(),
                    true,
                    true
            );
            certificateIntegrationService.createCertificate(certificateDataIntegrationRequest);
            save(trustedCertificate);
        }
        TrustedCertificateLog certLog = changeControlCert.getTrustedCertificateLog();
        certLog.setCertificateId(trustedCertificate.getCertificateId());
        changeControlCert.setTrustedCertificateLog(certLog);
        return trustedCertificate;
    }

    @Override
    public Page<CertType> findCertificates(Pageable pageable, String certName, String thumbprint) {
        LOG.info("Search trusted certificates by trusted certificate name {} and thumbprint {}",
                certName, thumbprint);
        List<CertType> certificates = new ArrayList<>();
        Specification<TrustedCertificate> specification = Specification
                .where(
                        GenericSpecification.<TrustedCertificate>filter("certificateName", certName))
                .and(
                        Specification
                                .where(
                                        GenericSpecification.<TrustedCertificate>filter("thumbprint", thumbprint))
                                .or(
                                        GenericSpecification.filter("thumbprint256", thumbprint))
                );
        List<TrustedCertificate> certificateList = trustedCertificateRepository.findAll(specification);
        List<ChangeControlCert> ccList = changeControlCertService.findPendingChangeControls(certName, thumbprint);
        certificateList.removeIf(cert ->
                ccList.stream().anyMatch(control -> control.getResultMeta3().equals(cert.getThumbprint256())));
        certificates.addAll(certificateList);
        certificates.addAll(ccList);
        certificates.sort(Comparator.comparing(CertType::nameForSorting, String.CASE_INSENSITIVE_ORDER));
        return ListToPageConverter.convertListToPage(certificates, pageable);
    }

    @Override
    public boolean fieldValueExists(Object value, String fieldName) {
        if (fieldName.equals("CERTIFICATE_NAME"))
            return trustedCertificateRepository.existsByCertificateName(String.valueOf(value));
        return false;
    }
}
