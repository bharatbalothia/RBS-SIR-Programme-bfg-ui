package com.ibm.sterling.bfg.app.change.service;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import com.ibm.sterling.bfg.app.change.model.ChangeControlConstants;
import com.ibm.sterling.bfg.app.change.model.ChangeControlStatus;
import com.ibm.sterling.bfg.app.change.repository.ChangeControlRepository;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.repository.EntityLogRepository;
import com.ibm.sterling.bfg.app.service.EntityServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChangeControlService {
    private static final Logger LOGGER = LogManager.getLogger(EntityServiceImpl.class);

    @Autowired
    private ChangeControlRepository controlRepository;

    @Autowired
    private EntityLogRepository entityLogRepository;

    public List<ChangeControl> listAll() {
        return controlRepository.findAll();
    }

    public Optional<ChangeControl> findById(String id) {
        LOGGER.info("change control by id {}", id);
        return controlRepository.findById(id);
    }

    public ChangeControl save(ChangeControl changeControl) throws Exception {
          return controlRepository.save(changeControl);
    }

    public ChangeControl updateStatus(String changeControlId, ChangeControlStatus status) throws Exception {
            ChangeControl controlFromBD = controlRepository.findById(changeControlId)
                    .orElseThrow(() -> new Exception("ChangeControl (id = " + changeControlId + ") not found"));
            controlFromBD.setStatus(status);
            controlRepository.save(controlFromBD);
            return controlFromBD;
    }

    public void setApproveInfo(ChangeControl changeControl,
                                        String user,
                                        String comments,
                                        ChangeControlStatus status) {
        changeControl.setApprover(user);
        changeControl.setApproverComments(comments);
        changeControl.setStatus(status);

        controlRepository.save(changeControl);
    }

    public boolean isNameUnique(String entityName) {
        return controlRepository
                .findAll()
                .stream()
                .noneMatch(changeControl ->
                        changeControl.getResultMeta1().equalsIgnoreCase(entityName) &&
                                changeControl.getObjectType().equals(ChangeControlConstants.OBJECT_TYPE) &&
                                changeControl.getStatus().equals(ChangeControlStatus.PENDING)
                );
    }

    public List<Entity> findAllPendingEntities(Pageable pageable) {
        return controlRepository
                .findByStatus(ChangeControlStatus.PENDING)
                .stream()
                .map(changeControl -> changeControl.getEntityFromEntityLog())
                .collect(Collectors.toList());
    }
}
