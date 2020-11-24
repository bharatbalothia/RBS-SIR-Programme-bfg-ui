package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
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

@Service
@Transactional
public class ChangeControlService {
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlService.class);

    @Autowired
    private ChangeControlRepository changeControlRepository;

    @Autowired
    private EntityValidationComponent entityValidation;

    public List<ChangeControl> listAll() {
        return changeControlRepository.findAll();
    }

    public Optional<ChangeControl> findById(String id) {
        LOGGER.info("change control by id {}", id);
        return changeControlRepository.findById(id);
    }

    public Optional<ChangeControl> findPendingChangeById(String id) {
        LOGGER.info("pending change control by id {}", id);
        return changeControlRepository.findByChangeIDAndStatus(id, ChangeControlStatus.PENDING);
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

    public void setApproveInfo(ChangeControl changeControl, String user,
                               String comments, ChangeControlStatus status) {
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);
        changeControlRepository.save(changeControl);
    }

    public List<ChangeControl> findAllPending() {
        List<ChangeControl> pendingChangeControlList = changeControlRepository.findByStatus(ChangeControlStatus.PENDING);
        Collections.sort(pendingChangeControlList);
        return pendingChangeControlList;
    }

    public List<ChangeControl> findAllPending(String entity, String service, String swiftDN) {
        Specification<ChangeControl> specification = Specification
                .where(
                        GenericSpecification.<ChangeControl>filter(entity, "resultMeta1"))
                .and(
                        GenericSpecification.filter(service, "resultMeta2"))
                .and(
                        GenericSpecification.filter(ChangeControlStatus.PENDING.getStatusText(), "status"))
                .and(
                        GenericSpecification.filter(swiftDN, "swiftDN")
                );
        return changeControlRepository.findAll(specification);
    }

    public void updateChangeControl(ChangeControl changeControl, Entity entity) {
        Operation operation = changeControl.getOperation();
        if (!operation.equals(Operation.DELETE)) {
            entityValidation.validateEntity(entity, operation);
        }
        if (changeControl.getStatus().equals(ChangeControlStatus.PENDING)
                && (operation.equals(Operation.CREATE) ||
                operation.equals(Operation.UPDATE))) {
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

    public boolean deleteChangeControl(ChangeControl changeControl) {
        if (changeControl.getStatus().equals(ChangeControlStatus.PENDING)) {
            changeControlRepository.delete(changeControl);
            if (!findById(changeControl.getChangeID()).isPresent()) {
                return true;
            }
        }
        return false;
    }
}
