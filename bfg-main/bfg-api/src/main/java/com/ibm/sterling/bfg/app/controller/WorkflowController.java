package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.service.file.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/workflow")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
public class WorkflowController {

    @Autowired
    private SearchService searchService;

    @GetMapping("{id}/steps")
    public Page<WorkflowStep> getWorkflowSteps(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                               @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                               @PathVariable Integer id) throws JsonProcessingException {
        return searchService.getWorkflowSteps(id, page, size);
    }

    @GetMapping("bp-details")
    public BPDetails getBPDetails(@RequestParam String identifier) throws JsonProcessingException {
        return searchService.getBPDetails(identifier);
    }

    @GetMapping("bp-header")
    public ResponseEntity<Map<String, String>> getBPHeader(@RequestParam Integer wfdVersion, @RequestParam Integer wfdID)
            throws JsonProcessingException {
        return ok(searchService.getBPHeader(wfdVersion, wfdID));
    }

    @GetMapping("document-content")
    public ResponseEntity<Document> getDocumentContent(@RequestParam(value = "id") String documentId)
            throws JsonProcessingException {
        return ok(searchService.getDocumentById(documentId.isEmpty() ? null : documentId));
    }

}
