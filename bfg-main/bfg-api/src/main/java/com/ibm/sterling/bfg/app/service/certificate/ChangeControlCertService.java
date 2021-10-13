package com.ibm.sterling.bfg.app.service.certificate;

import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.certificate.ChangeControlCertNotFoundException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusPendingException;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateLog;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.validation.CertificateValidationComponent;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.service.GenericSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.PENDING;

@Service
@Transactional(readOnly = true)
public class ChangeControlCertService {
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlCertService.class);

    @Autowired
    private ChangeControlCertRepository changeControlCertRepository;

    @Autowired
    private CertificateValidationComponent certificateValidation;

    public List<ChangeControlCert> listAll() {
        return changeControlCertRepository.findAll();
    }

    public ChangeControlCert getChangeControlCertById(String id) {
        LOGGER.info("Certificate change control by id {}", id);
        ChangeControlCert changeControlCert = changeControlCertRepository.findById(id)
                .orElseThrow(ChangeControlCertNotFoundException::new);
        checkStatusOfChangeControl(changeControlCert);
        return changeControlCert;
    }

    @Transactional
    public ChangeControlCert save(ChangeControlCert changeControl) {
        return changeControlCertRepository.save(changeControl);
    }

    @Transactional
    public ChangeControlCert updateStatus(String changeControlId, ChangeControlStatus status) {
        ChangeControlCert controlFromBD = changeControlCertRepository.findById(changeControlId)
                .orElseThrow(CertificateNotFoundException::new);
        controlFromBD.setStatus(status);
        changeControlCertRepository.save(controlFromBD);
        return controlFromBD;
    }

    @Transactional
    public void setApproveInfo(ChangeControlCert changeControl, String user,
                               String comments, ChangeControlStatus status) {
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);
        changeControl.getTrustedCertificateLog().setCertificate(null);
        changeControlCertRepository.save(changeControl);
    }

    public List<ChangeControlCert> findPendingChangeControlsAsc(String certName, String thumbprint) {
        List<ChangeControlCert> pendingCertChangeControlList = findPendingChangeControls(certName, thumbprint);
        Collections.sort(pendingCertChangeControlList);
        return pendingCertChangeControlList;
    }

    public List<ChangeControlCert> findPendingChangeControls(String certName, String thumbprint) {
        Specification<ChangeControlCert> specification = Specification
                .where(
                        GenericSpecification.<ChangeControlCert>filter("resultMeta1", certName))
                .and(
                        Specification
                                .where(
                                        GenericSpecification.<ChangeControlCert>filter("resultMeta2", thumbprint))
                                .or(
                                        GenericSpecification.filter("resultMeta3", thumbprint))
                )
                .and(
                        GenericSpecification.filter("status", ChangeControlStatus.PENDING.getStatusText())
                );
        return changeControlCertRepository.findAll(specification);
    }

    @Transactional
    public TrustedCertificate updateChangeControlCert(ChangeControlCert changeControlCert, String certName, String changerComments) {
        LOGGER.info("Updating pending {}", changeControlCert);
        checkStatusOfChangeControl(changeControlCert);
        if (!changeControlCert.getOperation().equals(Operation.DELETE)) {
            TrustedCertificateLog trustedCertificateLog = changeControlCert.getTrustedCertificateLog();
            Optional.ofNullable(certName).ifPresent(name -> {
                trustedCertificateLog.setCertificateName(name);
                changeControlCert.setResultMeta1(name);
            });
            TrustedCertificate trustedCertificate = changeControlCert.convertTrustedCertificateLogToTrustedCertificate();
            certificateValidation.validateCertificate(trustedCertificate);

            Optional.ofNullable(changerComments).ifPresent(comments -> {
                changeControlCert.setChangerComments(comments);
                trustedCertificate.setChangerComments(changerComments);
            });
            save(changeControlCert);
            return trustedCertificate;
        }
        return null;
    }

    @Transactional
    public void deleteChangeControl(ChangeControlCert changeControlCert) {
        LOGGER.info("Deleting pending {}", changeControlCert);
        checkStatusOfChangeControl(changeControlCert);
        changeControlCertRepository.delete(changeControlCert);
    }

    private void checkStatusOfChangeControl(ChangeControlCert changeControlCert) {
        LOGGER.info("Checking status of change control {}", changeControlCert);
        if (!PENDING.equals(changeControlCert.getStatus())) {
            throw new StatusNotPendingException("Status of change control " + changeControlCert.getChangeID() +
                    " is not pending");
        }
    }

    public void checkOnPendingState(String certName) {
        LOGGER.info("Checking if trusted certificate {} in a pending state", certName);
        if (changeControlCertRepository.existsByResultMeta1AndStatus(certName, PENDING))
            throw new StatusPendingException("Trusted certificate " + certName + " is in pending state");
    }
}
