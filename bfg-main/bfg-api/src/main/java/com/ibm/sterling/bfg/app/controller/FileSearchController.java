package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.File;
import com.ibm.sterling.bfg.app.model.file.FileSearchCriteria;
import com.ibm.sterling.bfg.app.service.FileSearchService;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/files")
public class FileSearchController {

    @Autowired
    private FileSearchService fileSearchService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public Page<File> getFiles(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                               @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                               @RequestBody(required = false) FileSearchCriteria fileSearchCriteria)
            throws JsonProcessingException {
        return fileSearchService.getFilesList(
                Optional.ofNullable(fileSearchCriteria).orElse(new FileSearchCriteria()), page, size);
    }

    @GetMapping("file-criteria-data")
    public ResponseEntity<Map<String, List<String>>> getFileCriteriaData(
            @RequestParam(value = "service", required = false) String service,
            @RequestParam(value = "outbound", required = false) Boolean outbound) throws JsonProcessingException {
        return ok(propertyService.getFileCriteriaData(service, outbound));
    }

}
