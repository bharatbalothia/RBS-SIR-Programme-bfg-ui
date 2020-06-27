package com.ibm.sterling.bfg.app.service.certificate;

import com.ibm.sterling.bfg.app.exception.CertificateNotFoundException;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
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

@Service
public class ChangeControlCertService {
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlCertService.class);

    @Autowired
    private ChangeControlCertRepository controlCertRepository;

    public List<ChangeControlCert> listAll() {
        return controlCertRepository.findAll();
    }

    public Optional<ChangeControlCert> findById(String id) {
        LOGGER.info("Certificate change control by id {}", id);
        return controlCertRepository.findById(id);
    }

    public ChangeControlCert save(ChangeControlCert changeControl) {
        return controlCertRepository.save(changeControl);
    }

    public ChangeControlCert updateStatus(String changeControlId, ChangeControlStatus status) throws Exception {
        ChangeControlCert controlFromBD = controlCertRepository.findById(changeControlId)
                .orElseThrow(CertificateNotFoundException::new);
        controlFromBD.setStatus(status);
        controlCertRepository.save(controlFromBD);
        return controlFromBD;
    }

    public void setApproveInfo(ChangeControlCert changeControl,
                               String user,
                               String comments,
                               ChangeControlStatus status) {
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);
        controlCertRepository.save(changeControl);
    }

    public boolean isNameUnique(String entityName) {
        return controlCertRepository
                .findAll()
                .stream()
                .noneMatch(changeControl ->
                        changeControl.getResultMeta1().equalsIgnoreCase(entityName) &&
                                changeControl.getStatus().equals(ChangeControlStatus.PENDING)
                );
    }

    public List<ChangeControlCert> findAllPending() {
        return convertStreamToList(controlCertRepository
                .findByStatus(ChangeControlStatus.PENDING)
                .stream());
    }

    private List<ChangeControlCert> convertStreamToList(Stream<ChangeControlCert> stream) {
        return stream
                .sorted()
                .collect(Collectors.toList());
    }

    public List<ChangeControlCert> findAllPending(String entity, String service) {
        Specification<ChangeControlCert> specification = Specification
                .where(
                        GenericSpecification.<ChangeControlCert>filter(entity, "resultMeta1"))
                .and(
                        GenericSpecification.filter(service, "resultMeta2"))
                .and(
                        GenericSpecification.filter(ChangeControlStatus.PENDING.getStatusText() , "status")
                );
        return convertStreamToList(controlCertRepository
                .findAll(specification)
                .stream());
    }
}
