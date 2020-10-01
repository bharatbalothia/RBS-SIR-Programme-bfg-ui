package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.exception.ChangeControlNotFoundException;
import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.entity.*;
import com.ibm.sterling.bfg.app.exception.InvalidUserForUpdatePendingEntityException;
import com.ibm.sterling.bfg.app.model.EntityType;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.service.*;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/entities")
public class EntityController {

    @Autowired
    private ErrorConfig errorConfig;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private ChangeControlService changeControlService;

    @Autowired
    private TransmittalService transmittalService;

    @GetMapping
    public Page<EntityType> getEntities(@RequestParam(value = "service", defaultValue = "", required = false) String serviceName,
                                        @RequestParam(value = "entity", defaultValue = "", required = false) String entityName,
                                        @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return entityService.findEntities(PageRequest.of(page, size), entityName, serviceName);
    }

    @GetMapping("pending")
    public Page<Object> getPendingEntities(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                           @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return ListToPageConverter.convertListToPage(
                new ArrayList<>(changeControlService.findAllPending()), PageRequest.of(page, size));
    }

    @PostMapping("pending")
    @PreAuthorize("@entityPermissionEvaluator.checkApprovePermission(#approve)")
    public ResponseEntity<Entity> postPendingEntities(@RequestBody Map<String, Object> approve) throws Exception {
        ChangeControl changeControl = changeControlService.findById(String.valueOf(approve.get("changeID")))
                .orElseThrow(EntityNotFoundException::new);
        return Optional.ofNullable(entityService.getEntityAfterApprove(
                changeControl,
                String.valueOf(approve.get("approverComments")),
                ChangeControlStatus.valueOf(String.valueOf(approve.get("status"))))
        ).map(record -> ok()
                .body(record))
                .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityById(@PathVariable(name = "id") int id) {
        return entityService.findById(id)
                .map(record -> ok()
                        .body(record))
                .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("pending/{id}")
    public ResponseEntity<Entity> getPendingEntityById(@PathVariable(name = "id") String id) {
        return changeControlService.findById(id)
                .map(record -> ok()
                        .body(record.convertEntityLogToEntity()))
                .orElseThrow(ChangeControlNotFoundException::new);
    }

    @PostMapping
    @PreAuthorize("@entityPermissionEvaluator.checkCreatePermission(#entity)")
    public ResponseEntity<Entity> createEntity(@RequestBody Entity entity) {
        return ok(entityService.saveEntityToChangeControl(entity, Operation.CREATE));
    }

    @PutMapping("pending/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkEditPendingPermission(#id, #entity.getService())")
    public ResponseEntity<ChangeControl> updatePendingEntity(@RequestBody Entity entity, @PathVariable String id) {
        ChangeControl changeControl = changeControlService.findById(id)
                .orElseThrow(ChangeControlNotFoundException::new);
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(changeControl.getChanger()))
            throw new InvalidUserForUpdatePendingEntityException();
        changeControlService.updateChangeControl(changeControl, entity);
        return ok(changeControl);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkEditPermission(#id)")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> {
                    entity.setEntity(record.getEntity());
                    entity.setService(record.getService());
                    return ok(entityService.saveEntityToChangeControl(entity, Operation.UPDATE));
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkDeletePermission(#id)")
    public ResponseEntity<?> deleteEntity(@PathVariable int id, @RequestParam(required = false) String changerComments) {
        Entity entity = entityService.findById(id).orElseThrow(EntityNotFoundException::new);
        Optional.ofNullable(changerComments).ifPresent(entity::setChangerComments);
        return ok(entityService.saveEntityToChangeControl(entity, Operation.DELETE));
    }

    @GetMapping("/existence")
    public ResponseEntity<?> isExistingEntity(@RequestParam String service, @RequestParam String entity) {
        return ok(entityService.existsByServiceAndEntity(service, entity));
    }

    @GetMapping("inbound-request-type")
    public ResponseEntity<List<String>> getInboundRequestType() throws JsonProcessingException {
        return ok(propertyService.getInboundRequestType());
    }

    @GetMapping("file-type")
    public ResponseEntity<List<String>> getFileType() throws JsonProcessingException {
        return ok(propertyService.getFileType());
    }

    @GetMapping("mq-details")
    public ResponseEntity<Map<String, List<String>>> getMqDetails() throws JsonProcessingException {
        return ok(propertyService.getMQDetails());
    }

    @PostMapping("transmit")
    @PreAuthorize("hasAuthority('SFG_SCT_ICFOutbound')")
    public ResponseEntity<Map<String, Object>> transmit(@RequestBody Transmittal transmittal) throws JsonProcessingException {
        return ok(transmittalService.transmit(transmittal));
    }

}
