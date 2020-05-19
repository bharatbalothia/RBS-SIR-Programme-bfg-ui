package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/entities")
public class EntityController {

    private static final Logger LOG = LogManager.getLogger(EntityController.class);

    @Autowired
    private EntityService entityService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityById(@PathVariable(name = "id") int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Entity> createEntity(@RequestBody Entity entity) {
        return ResponseEntity.ok(entityService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entity> updateEntity(@RequestBody Entity entity, @PathVariable int id) {
        return entityService.findById(id)
                .map(record -> ResponseEntity.ok().body(entityService.save(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable int id) {
        return entityService.findById(id)
                .map(record -> {
                    entityService.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
