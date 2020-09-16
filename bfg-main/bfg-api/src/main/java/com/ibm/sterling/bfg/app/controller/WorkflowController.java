package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/workflow")
public class WorkflowController {

    @Autowired
    private SearchService searchService;

    @GetMapping("workflow-steps")
    public ResponseEntity<List<WorkflowStep>> getWorkflowSteps(@RequestParam(value = "id") Integer workFlowId)
            throws JsonProcessingException {
        return ok(searchService.getWorkflowSteps(workFlowId));
    }

}
