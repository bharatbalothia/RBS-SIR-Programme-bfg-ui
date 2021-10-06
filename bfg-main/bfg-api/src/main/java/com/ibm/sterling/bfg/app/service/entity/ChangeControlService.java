package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusPendingException;
import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.EntityLog;
import com.ibm.sterling.bfg.app.model.validation.EntityValidationComponent;
import com.ibm.sterling.bfg.app.repository.entity.ChangeControlRepository;
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
public class ChangeControlService {
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlService.class);

    @Autowired
    private ChangeControlRepository changeControlRepository;

    @Autowired
    private EntityValidationComponent entityValidation;

//    public List<ChangeControl> listAll() {
//        return changeControlRepository.findAll();
//    }

    public Optional<ChangeControl> findById(String id) {
        LOGGER.info("Find change control by id {}", id);
        return changeControlRepository.findById(id);
    }

//    public Optional<ChangeControl> findPendingChangeById(String id) {
//        LOGGER.info("pending change control by id {}", id);
//        return changeControlRepository.findByChangeIDAndStatus(id, ChangeControlStatus.PENDING);
//    }

    @Transactional
    public ChangeControl save(ChangeControl changeControl) {
        LOGGER.info("Persist {}", changeControl);
        return changeControlRepository.save(changeControl);
    }

//    @Transactional
//    public ChangeControl updateStatus(String changeControlId, ChangeControlStatus status) throws Exception {
//        ChangeControl controlFromBD = changeControlRepository.findById(changeControlId)
//                .orElseThrow(() -> new Exception("ChangeControl (id = " + changeControlId + ") not found"));
//        controlFromBD.setStatus(status);
//        changeControlRepository.save(controlFromBD);
//        return controlFromBD;
//    }

    @Transactional
    public void setApproveInfo(ChangeControl changeControl, String user,
                               String comments, ChangeControlStatus status) {
        LOGGER.info("Set approve information - approve user : {}, comments : {}, status : {} for {}",
                user, comments, status, changeControl);
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);
        changeControlRepository.save(changeControl);
    }

    public List<ChangeControl> findPendingChangeControlsAsc(String entity, String service, String swiftDN) {
        List<ChangeControl> pendingChangeControlList = findPendingChangeControls(entity, service, swiftDN);
        Collections.sort(pendingChangeControlList);
        LOGGER.info("Sort change control list by ResultMeta1 {}", entity);
        return pendingChangeControlList;
    }

    public List<ChangeControl> findPendingChangeControls(String entity, String service, String swiftDN) {
        LOGGER.info("Search pending change control by entity: {}, service: {}, swiftDN: {}",
                entity, service, swiftDN);
        Specification<ChangeControl> specification = Specification
                .where(
                        GenericSpecification.<ChangeControl>filter("resultMeta1", entity))
                .and(
                        GenericSpecification.filter("resultMeta2", service))
                .and(
                        GenericSpecification.filter("status", ChangeControlStatus.PENDING.getStatusText()))
                .and(
                        GenericSpecification.filter("swiftDN", swiftDN)
                );
        return changeControlRepository.findAll(specification);
    }

    @Transactional
    public void updateChangeControl(ChangeControl changeControl, Entity entity) {
        LOGGER.info("Updating pending {}", changeControl);
        checkStatusOfChangeControl(changeControl);
        Operation operation = changeControl.getOperation();
        if (!operation.equals(Operation.DELETE)) {
            entityValidation.validateEntity(entity, operation);
            changeControl.setChangerComments(entity.getChangerComments());
            EntityLog entityLog = new EntityLog(entity);
            if (operation.equals(Operation.CREATE)) {
                changeControl.setResultMeta1(entityLog.getEntity());
                changeControl.setResultMeta2(entityLog.getService());
            }
            entityLog.setEntityLogId(changeControl.getEntityLog().getEntityLogId());
            changeControl.setEntityLog(entityLog);
            save(changeControl);
        }
    }

    @Transactional
    public void deleteChangeControl(ChangeControl changeControl) {
        LOGGER.info("Deleting pending {}", changeControl);
        checkStatusOfChangeControl(changeControl);
        changeControlRepository.delete(changeControl);
    }

    public void checkStatusOfChangeControl(ChangeControl changeControl) {
        LOGGER.info("Checking status of change control {}", changeControl);
        if (!PENDING.equals(changeControl.getStatus())) {
            throw new StatusNotPendingException();
        }
    }

    public void checkOnPendingState(String entityName, String service) {
        LOGGER.info("Checking if entity {} in a pending state", entityName);
        if (changeControlRepository.existsByResultMeta1AndResultMeta2AndStatus(entityName, service, PENDING))
            throw new StatusPendingException();
    }
}
