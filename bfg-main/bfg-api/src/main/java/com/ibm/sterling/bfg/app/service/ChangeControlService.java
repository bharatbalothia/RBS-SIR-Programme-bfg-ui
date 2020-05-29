package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlConstants;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.repository.ChangeControlRepository;
import com.ibm.sterling.bfg.app.repository.EntityLogRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChangeControlService {
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlService.class);

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

    public ChangeControl save(ChangeControl changeControl) {
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

    public List<ChangeControl> findAllPending() {
        return convertStreamToList(controlRepository
                .findByStatus(ChangeControlStatus.PENDING)
                .stream());
    }

    public List<ChangeControl> findAllPendingEntities(String service) {
        return convertStreamToList(controlRepository
                .findByStatusAndResultMeta2(ChangeControlStatus.PENDING, service)
                .stream());
    }

    public List<ChangeControl> findAllPendingByEntity(String entity) {
        return convertStreamToList(controlRepository
                .findByStatusAndResultMeta1(ChangeControlStatus.PENDING, entity)
                .stream());
    }

    private List<ChangeControl> convertStreamToList(Stream<ChangeControl> stream) {
        return stream
                .sorted()
                .collect(Collectors.toList());
    }
}
