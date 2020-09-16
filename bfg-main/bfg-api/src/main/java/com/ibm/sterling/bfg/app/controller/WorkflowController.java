package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.service.SearchService;
import com.ibm.sterling.bfg.app.utils.ListToPageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

}
