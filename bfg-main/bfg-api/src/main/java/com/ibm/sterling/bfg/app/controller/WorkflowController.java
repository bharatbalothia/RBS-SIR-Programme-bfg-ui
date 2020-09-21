package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.service.SearchService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/workflow")
public class WorkflowController {

    @Autowired
    private SearchService searchService;

    @GetMapping("{id}/steps")
    public Page<WorkflowStep> getWorkflowSteps(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                               @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                               @PathVariable Integer id) throws JsonProcessingException {
        return ListToPageConverter.convertListToPage(
                new ArrayList<>(searchService.getWorkflowSteps(id)), PageRequest.of(page, size));
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

}
