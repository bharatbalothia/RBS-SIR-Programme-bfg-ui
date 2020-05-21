package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
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
import java.util.List;
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

    @CrossOrigin
    @GetMapping("pending")
    public List<Entity> getPendingEntities(Pageable pageable) {
        return changeControlService.findAllPendingEntities(pageable);
    }

    @CrossOrigin
    @PostMapping("pending/{changeId}")
    public ResponseEntity<Entity> PendingEntities(@PathVariable(name = "changeId" ) String changeId,
                                                     @RequestBody Map<String, String> approverComments) throws Exception {
        return Optional.ofNullable(entityService.getEntityAfterApprove(changeId, approverComments.get("approverComments")))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(EntityNotFoundException::new);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityById(@PathVariable(name = "id") int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(EntityNotFoundException::new);
    }
    @CrossOrigin
    @PostMapping
    public ResponseEntity<Entity> createEntity(@Valid @RequestBody Entity entity) {
        return ResponseEntity.ok(entityService.saveEntityToChangeControl(entity));
    }
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok().body(entityService.save(entity)))
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
