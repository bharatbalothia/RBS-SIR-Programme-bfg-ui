package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.model.event.AuditEvent;
import com.ibm.sterling.bfg.app.model.event.AuditEventCriteria;
import com.ibm.sterling.bfg.app.service.PropertyService;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/news")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
public class AuditEventController {

    @Autowired
    private AdminAuditService auditService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public List<AuditEvent> getAuditEvents(
            @RequestBody(required = false) AuditEventCriteria auditEventCriteria) {
        return auditService.getAuditEvents(
                Optional.ofNullable(auditEventCriteria).orElse(new AuditEventCriteria()));
    }

    @GetMapping("event-criteria-data")
    public ResponseEntity<Map<String, List<Object>>> getEventCriteriaData() {
        return ok(propertyService.getEventCriteriaData());
    }

}
