package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.File;
import com.ibm.sterling.bfg.app.service.FileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/files")
public class FileSearchController {

    @Autowired
    private FileSearchService fileSearchService;

    @GetMapping
    public Page<File> getFiles(@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                               @RequestParam(value = "page", defaultValue = "0", required = false) Integer page)
            throws JsonProcessingException {
        return fileSearchService.getFilesList(PageRequest.of(page, size));
    }
}
