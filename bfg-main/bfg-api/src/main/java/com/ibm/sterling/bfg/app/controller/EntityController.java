package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.entity.ChangeControlNotFoundException;
import com.ibm.sterling.bfg.app.exception.entity.EntityNotFoundException;
import com.ibm.sterling.bfg.app.exception.entity.FieldsValidationException;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.*;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.service.PropertyService;
import com.ibm.sterling.bfg.app.service.entity.ChangeControlService;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import com.ibm.sterling.bfg.app.service.entity.TransmittalService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/entities")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
public class EntityController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private ChangeControlService changeControlService;

    @Autowired
    private TransmittalService transmittalService;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @GetMapping
    @PreAuthorize("hasAuthority('SFG_UI_SCT_ENTITY')")
    public Page<EntityType> getEntities(@RequestParam(value = "entity", defaultValue = "", required = false) String entityName,
                                        @RequestParam(value = "service", defaultValue = "", required = false) String serviceName,
                                        @RequestParam(value = "swiftDN", defaultValue = "", required = false) String swiftDN,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                        @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return entityService.findEntities(PageRequest.of(page, size), entityName, serviceName, swiftDN);
    }

    @GetMapping("pending")
    @PreAuthorize("hasAuthority('SFG_UI_SCT_ENTITY')")
    public Page<ChangeControl> getPendingEntities(@RequestParam(value = "entity", defaultValue = "", required = false) String entityName,
                                                  @RequestParam(value = "service", defaultValue = "", required = false) String serviceName,
                                                  @RequestParam(value = "swiftDN", defaultValue = "", required = false) String swiftDN,
                                                  @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                  @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return ListToPageConverter.convertListToPage(
                new ArrayList<>(changeControlService.findPendingChangeControlsAsc(entityName, serviceName, swiftDN)),
                PageRequest.of(page, size)
        );
    }

    @PostMapping("pending")
    @PreAuthorize("@entityPermissionEvaluator.checkApprovePermission(#approve)")
    public ResponseEntity<Entity> postPendingEntities(@RequestBody Map<String, Object> approve) throws Exception {
        ChangeControl changeControl = changeControlService.findById(String.valueOf(approve.get("changeID")))
                .orElseThrow(EntityNotFoundException::new);
        return Optional.ofNullable(entityService.getEntityAfterApprove(
                changeControl,
                Objects.toString(approve.get("approverComments"), null),
                ChangeControlStatus.valueOf(String.valueOf(approve.get("status"))))
        ).map(ResponseEntity::ok)
                .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SFG_UI_SCT_ENTITY')")
    public ResponseEntity<Entity> getEntityById(@PathVariable int id) {
        return entityService.findById(id)
                .map(record -> {
                    record.setInboundRequestType(
                            propertyService.getRestoredInboundRequestType(record.getInboundRequestType())
                    );
                    if (StringUtils.isEmpty(record.getInboundRequestorDN()) ||
                            StringUtils.isEmpty(record.getInboundResponderDN()))
                        record.setRouteInbound(Boolean.FALSE);
                    return ok(record);
                }).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("pending/{id}")
    @PreAuthorize("hasAuthority('SFG_UI_SCT_ENTITY')")
    public ResponseEntity<Entity> getPendingEntityById(@PathVariable String id) {
        return changeControlService.findPendingChangeById(id)
                .map(record -> {
                    Entity entity = record.convertEntityLogToEntity();
                    entity.setInboundRequestType(
                            propertyService.getRestoredInboundRequestType(entity.getInboundRequestType())
                    );
                    return ok(entity);
                }).orElseThrow(ChangeControlNotFoundException::new);
    }

    @GetMapping("changeControl/{id}")
    @PreAuthorize("hasAuthority('SFG_UI_SCT_ENTITY')")
    public ResponseEntity<ChangeControl> getChangeControlById(@PathVariable String id) {
        return changeControlService.findPendingChangeById(id)
                .map(changeControl -> {
                    EntityLog entityLog = changeControl.getEntityLog();
                    entityLog.setInboundRequestType(
                            propertyService.getRestoredInboundRequestType(entityLog.getInboundRequestType()));
                    changeControl.setEntityLog(entityLog);
                    return ok(changeControl);
                }).orElseThrow(ChangeControlNotFoundException::new);
    }

    @PostMapping
    @PreAuthorize("@entityPermissionEvaluator.checkCreatePermission(#entity)")
    public ResponseEntity<Entity> createEntity(@RequestBody Entity entity) {
        return ok(entityService.saveEntityToChangeControl(entity, Operation.CREATE));
    }

    @PutMapping("pending/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkEditPendingEntityChangePermission(#id, #entity.getService())")
    public ResponseEntity<ChangeControl> updatePendingEntity(@RequestBody Entity entity, @PathVariable String id) {
        ChangeControl changeControl = changeControlService.findById(id)
                .orElseThrow(ChangeControlNotFoundException::new);
        apiDetailsHandler.checkPermissionForUpdateChangeControl(changeControl.getChanger());
        entityService.updatePendingEntity(changeControl, entity);
        return ok(changeControl);
    }

    @DeleteMapping("pending/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkDeletePendingEntityChangePermission(#id)")
    public ResponseEntity<?> deletePendingEntity(@PathVariable String id) {
        ChangeControl changeControl = changeControlService.findById(id)
                .orElseThrow(ChangeControlNotFoundException::new);
        apiDetailsHandler.checkPermissionForUpdateChangeControl(changeControl.getChanger());
        entityService.cancelPendingEntity(changeControl);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkEditPermission(#id)")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> {
                    entity.setEntity(record.getEntity());
                    entity.setService(record.getService());
                    return ok(entityService.saveEntityToChangeControl(entity, Operation.UPDATE));
                }).orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@entityPermissionEvaluator.checkDeletePermission(#id)")
    public ResponseEntity<?> deleteEntity(@PathVariable int id, @RequestParam(required = false) String changerComments) {
        Entity entity = entityService.findById(id).orElseThrow(EntityNotFoundException::new);
        Optional.ofNullable(changerComments).ifPresent(entity::setChangerComments);
        return ok(entityService.saveEntityToChangeControl(entity, Operation.DELETE));
    }

    @GetMapping("/existence/entity-service")
    public ResponseEntity<?> isExistingEntity(@RequestParam String service, @RequestParam String entity) {
        return ok(entityService.existsByServiceAndEntity(service, entity));
    }

    @GetMapping("/existence/mailbox")
    public ResponseEntity<?> isExistingMailboxPathOut(@RequestParam String mailboxPathOut) {
        return ok(entityService.existsByMailboxPathOut(mailboxPathOut));
    }

    @GetMapping("/existence/queue")
    public ResponseEntity<?> isExistingMqQueueOut(@RequestParam String mqQueueOut) {
        return ok(entityService.existsByMqQueueOut(mqQueueOut));
    }

    @GetMapping("/existence/route-attributes")
    public ResponseEntity<?> getRequestType(@RequestParam String inboundRequestorDN,
                                            @RequestParam String inboundResponderDN,
                                            @RequestParam String inboundService,
                                            @RequestParam List<String> inboundRequestType) {
        Optional.ofNullable(entityService.getEntityWithAttributesOfRoutingRules(
                inboundRequestorDN, inboundResponderDN, inboundService, inboundRequestType))
                .ifPresent(entity -> {
                    throw new FieldsValidationException("Entity properties should be unique for requester DN, responder DN, service, and request types. " +
                            "These match the entity " + entity.getEntity() + ". Please correct the properties and try again, or cancel",
                            "routingRules");
                });
        return ok(Boolean.FALSE);
    }

    @GetMapping("inbound-request-type")
    public ResponseEntity<Map<String, String>> getInboundRequestType() throws JsonProcessingException {
        return ok(propertyService.getInboundRequestType());
    }

    @GetMapping("inbound-service")
    public ResponseEntity<String> getInboundService() throws JsonProcessingException {
        return ok(propertyService.getInboundService());
    }

    @GetMapping("participants")
    public ResponseEntity<List<String>> getParticipants(@RequestParam(required = false) Integer id) {
        return ok(entityService.findEntityNameForParticipants(id));
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
