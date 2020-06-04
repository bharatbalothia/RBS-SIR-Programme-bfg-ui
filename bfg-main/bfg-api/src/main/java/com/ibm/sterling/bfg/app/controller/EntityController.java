package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityType;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.validation.PutValidation;
import com.ibm.sterling.bfg.app.model.validation.PostValidation;
import com.ibm.sterling.bfg.app.service.ChangeControlService;
import com.ibm.sterling.bfg.app.service.EntityService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/entities")
public class EntityController {

    @Autowired
    private ErrorConfig errorConfig;

    @Autowired
    private EntityService entityService;

    @Autowired
    private ChangeControlService changeControlService;

    @CrossOrigin
    @GetMapping
    public Page<EntityType> getEntities(@RequestParam(value = "service", defaultValue = "", required = false) String serviceName,
                                        @RequestParam(value = "entity", defaultValue = "", required = false) String entityName,
                                        @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return entityService.findEntities(PageRequest.of(page, size), entityName, serviceName);
    }

    @CrossOrigin
    @GetMapping("pending")
    public Page<Object> getPendingEntities(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                           @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {
        return ListToPageConverter.convertListToPage(
                new ArrayList<>(changeControlService.findAllPending()), PageRequest.of(page, size));
    }

    @CrossOrigin
    @PostMapping("pending")
    public ResponseEntity<Entity> PendingEntities(@RequestBody Map<String, Object> approve) throws Exception {
        ChangeControlStatus status = ChangeControlStatus.valueOf((String) approve.get("status"));
        String changeId = (String) approve.get("changeID");
        return Optional.ofNullable(
                entityService.getEntityAfterApprove(
                        changeId,
                        (String) approve.get("approverComments"),
                        status))
                .map(record -> ResponseEntity.ok()
                        .body(record))
                .orElseThrow(EntityNotFoundException::new);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityById(@PathVariable(name = "id") int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok()
                        .body(record))
                .orElseThrow(EntityNotFoundException::new);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Entity> createEntity(@Validated({PostValidation.class}) @RequestBody Entity entity) {
        return ResponseEntity.ok(entityService.saveEntityToChangeControl(entity, Operation.CREATE));
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Entity> updateEntity(@Validated({PutValidation.class}) @RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok(entityService.saveEntityToChangeControl(entity, Operation.UPDATE)))
                .orElseThrow(EntityNotFoundException::new);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable int id) {
        return entityService.findById(id)
                .map(record -> {
                    entityService.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElseThrow(EntityNotFoundException::new);
    }

    @CrossOrigin
    @GetMapping("/existence")
    public ResponseEntity<?> isExistingEntity(@RequestParam String service, @RequestParam String entity) {
        return ResponseEntity.ok(entityService.existsByServiceAndEntity(service, entity));
    }
}
