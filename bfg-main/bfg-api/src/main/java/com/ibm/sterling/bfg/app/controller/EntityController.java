package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import com.ibm.sterling.bfg.app.change.model.ChangeControlStatus;
import com.ibm.sterling.bfg.app.change.service.ChangeControlService;
import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/entities")
public class EntityController {

    @Autowired
    private ErrorConfig errorConfig;

    @Autowired
    private EntityService entityService;

    @Autowired
    private ChangeControlService changeControlService;

    @GetMapping
    public Page<Entity> getEntities(@RequestParam(value = "service", required = false) String serviceName,
                                    @RequestParam(value = "entity", required = false) String entityName,
                                    Pageable pageable) {
        return Optional.ofNullable(serviceName)
                .map(service -> entityService.findEntitiesByService(service, pageable))
                .orElse(
                        Optional.ofNullable(entityName)
                                .map(entity -> entityService.findEntitiesByEntity(entity, pageable))
                                .orElse(entityService.findEntities(pageable))
                );
    }

    @GetMapping("pending")
    public ResponseEntity<List<ChangeControl>> getPendingEntities() {
        List<ChangeControl> list = changeControlService.findAllPending();
        return ResponseEntity.ok()
                .body(list);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityById(@PathVariable(name = "id") int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok()
                        .body(record))
                .orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<Entity> createEntity(@Valid @RequestBody Entity entity) {
        return ResponseEntity.ok(entityService.saveEntityToChangeControl(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok().body(entityService.save(entity)))
                .orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable int id) {
        return entityService.findById(id)
                .map(record -> {
                    entityService.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/existence")
    public ResponseEntity<?> isExistingEntity(@RequestParam String service, @RequestParam String entity) {
        return ResponseEntity.ok(entityService.existsByServiceAndEntity(service, entity));
    }

}
