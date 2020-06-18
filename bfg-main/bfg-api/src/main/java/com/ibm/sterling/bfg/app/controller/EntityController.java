package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityType;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.service.ChangeControlService;
import com.ibm.sterling.bfg.app.service.EntityService;
import com.ibm.sterling.bfg.app.service.PropertyService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Entity> postPendingEntities(@RequestBody Map<String, Object> approve) throws Exception {
        ChangeControlStatus status = ChangeControlStatus.valueOf((String) approve.get("status"));
        String changeId = (String) approve.get("changeID");
        return Optional.ofNullable(
                entityService.getEntityAfterApprove(
                        changeControlService.findById(changeId).orElseThrow(EntityNotFoundException::new),
                        (String) approve.get("approverComments"),
                        status))
                .map(record -> ok()
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

    @PostMapping
    public ResponseEntity<Entity> createEntity(@RequestBody Entity entity) {
        return ok(entityService.saveEntityToChangeControl(entity, Operation.CREATE));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> ok(entityService.saveEntityToChangeControl(entity, Operation.UPDATE)))
                .orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable int id, @RequestParam(required = false) String сhangerComments) {
        Entity entity = entityService.findById(id).orElseThrow(EntityNotFoundException::new);
        entity.setChangerComments(сhangerComments);
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

}
