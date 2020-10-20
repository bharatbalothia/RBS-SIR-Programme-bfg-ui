package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.EntityLog;
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

    public void updateChangeControl(ChangeControl changeControl, Entity entity) {
        if (changeControl.getStatus().equals(ChangeControlStatus.PENDING)
                && (changeControl.getOperation().equals(Operation.CREATE) ||
                changeControl.getOperation().equals(Operation.UPDATE))) {
            EntityLog entityLog = new EntityLog(entity);
            switch (changeControl.getOperation()) {
                case CREATE:
                    changeControl.setResultMeta1(entityLog.getEntity());
                    changeControl.setResultMeta2(entityLog.getService());
                    break;
                case UPDATE:
                    entityLog.setEntity(changeControl.getResultMeta1());
                    entityLog.setService(changeControl.getResultMeta2());
                    break;
            }
            entityLog.setEntityLogId(changeControl.getEntityLog().getEntityLogId());
            changeControl.setEntityLog(entityLog);
            save(changeControl);
        }
    }
}
