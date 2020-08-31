package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlConstants;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.repository.ChangeControlRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChangeControlService {
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlService.class);

    @Autowired
    private ChangeControlRepository changeControlRepository;

    public List<ChangeControl> listAll() {
        return changeControlRepository.findAll();
    }

    public Optional<ChangeControl> findById(String id) {
        LOGGER.info("change control by id {}", id);
        return changeControlRepository.findById(id);
    }

    public ChangeControl save(ChangeControl changeControl) {
        return changeControlRepository.save(changeControl);
    }

    public ChangeControl updateStatus(String changeControlId, ChangeControlStatus status) throws Exception {
        ChangeControl controlFromBD = changeControlRepository.findById(changeControlId)
                .orElseThrow(() -> new Exception("ChangeControl (id = " + changeControlId + ") not found"));
        controlFromBD.setStatus(status);
        changeControlRepository.save(controlFromBD);
        return controlFromBD;
    }

    public void setApproveInfo(ChangeControl changeControl,
                               String user,
                               String comments,
                               ChangeControlStatus status) {
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);
        changeControlRepository.save(changeControl);
    }

    public boolean isNameUnique(String entityName) {
        return changeControlRepository
                .findAll()
                .stream()
                .noneMatch(changeControl ->
                        changeControl.getResultMeta1().equalsIgnoreCase(entityName) &&
                                changeControl.getObjectType().equals(ChangeControlConstants.OBJECT_TYPE) &&
                                changeControl.getStatus().equals(ChangeControlStatus.PENDING)
                );
    }

    public List<ChangeControl> findAllPending() {
        List<ChangeControl> pendingChangeControlList = changeControlRepository.findByStatus(ChangeControlStatus.PENDING);
        Collections.sort(pendingChangeControlList);
        return pendingChangeControlList;
    }

    public List<ChangeControl> findAllPending(String entity, String service) {
        Specification<ChangeControl> specification = Specification
                .where(
                        GenericSpecification.<ChangeControl>filter(entity, "resultMeta1"))
                .and(
                        GenericSpecification.filter(service, "resultMeta2"))
                .and(
                        GenericSpecification.filter(ChangeControlStatus.PENDING.getStatusText(), "status")
                );
        return changeControlRepository.findAll(specification);
    }

}
