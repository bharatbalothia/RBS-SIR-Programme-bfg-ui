package com.ibm.sterling.bfg.app.service.certificate;

import com.ibm.sterling.bfg.app.exception.certificate.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.exception.certificate.ChangeControlCertNotFoundException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.PENDING;

@Service
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
        return changeControlCertRepository.findById(id)
                .orElseThrow(ChangeControlCertNotFoundException::new);
    }

    public ChangeControlCert save(ChangeControlCert changeControl) {
        return changeControlCertRepository.save(changeControl);
    }

    public ChangeControlCert updateStatus(String changeControlId, ChangeControlStatus status) {
        ChangeControlCert controlFromBD = changeControlCertRepository.findById(changeControlId)
                .orElseThrow(CertificateNotFoundException::new);
        controlFromBD.setStatus(status);
        changeControlCertRepository.save(controlFromBD);
        return controlFromBD;
    }

    public void setApproveInfo(ChangeControlCert changeControl, String user,
                               String comments, ChangeControlStatus status) {
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);
        changeControlCertRepository.save(changeControl);
    }

    public List<ChangeControlCert> findAllPending() {
        return convertStreamToList(changeControlCertRepository
                .findByStatus(ChangeControlStatus.PENDING)
                .stream());
    }

    private List<ChangeControlCert> convertStreamToList(Stream<ChangeControlCert> stream) {
        return stream
                .sorted()
                .collect(Collectors.toList());
    }

    public List<ChangeControlCert> findAllPending(String certName, String thumbprint, String thumbprint256) {
        Specification<ChangeControlCert> specification = Specification
                .where(
                        GenericSpecification.<ChangeControlCert>filter(certName, "resultMeta1"))
                .and(
                        GenericSpecification.filter(thumbprint, "resultMeta2"))
                .and(
                        GenericSpecification.filter(thumbprint256, "resultMeta3"))
                .and(
                        GenericSpecification.filter(ChangeControlStatus.PENDING.getStatusText(), "status")
                );
        return convertStreamToList(changeControlCertRepository
                .findAll(specification)
                .stream());
    }

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

    public void deleteChangeControl(ChangeControlCert changeControlCert) {
        LOGGER.info("Deleting pending {}", changeControlCert);
        checkStatusOfChangeControl(changeControlCert);
        changeControlCertRepository.delete(changeControlCert);
    }

    private void checkStatusOfChangeControl(ChangeControlCert changeControlCert) {
        if (!PENDING.equals(changeControlCert.getStatus())) {
            throw new StatusNotPendingException();
        }
    }
}
