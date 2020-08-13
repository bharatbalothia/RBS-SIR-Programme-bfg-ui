package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.FileNotFoundException;
import com.ibm.sterling.bfg.app.model.file.File;
import com.ibm.sterling.bfg.app.model.file.FileSearchCriteria;
import com.ibm.sterling.bfg.app.model.file.Transaction;
import com.ibm.sterling.bfg.app.service.FileSearchService;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public Page<File> getFiles(@Valid @RequestBody(required = false) FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        return fileSearchService.getFilesList(Optional.ofNullable(fileSearchCriteria).orElse(new FileSearchCriteria()));
    }

    @GetMapping("{id}")
    public ResponseEntity<File> getFileById(@PathVariable Integer id) throws JsonProcessingException {
        return ok(fileSearchService.getFileById(id)
                .orElseThrow(FileNotFoundException::new));
    }

    @GetMapping("{fileId}/transactions")
    public Page<Transaction> getTransactionsForFiles(@PathVariable Integer fileId,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                     @RequestParam(value = "page", defaultValue = "0", required = false) Integer page)
            throws JsonProcessingException {
        return fileSearchService.getTransactionsList(fileId, size, page);
    }

    @GetMapping("{fileId}/transactions/{transactionId}")
    public Transaction getTransactionById(@PathVariable Integer fileId,
                                          @PathVariable Integer transactionId)
            throws JsonProcessingException {
        return fileSearchService.getTransactionById(fileId, transactionId).orElseThrow(FileNotFoundException::new);
    }

    @GetMapping("file-criteria-data")
    public ResponseEntity<Map<String, List<Object>>> getFileCriteriaData(
            @RequestParam(value = "service", required = false) String service,
            @RequestParam(value = "outbound", required = false) Boolean outbound) throws JsonProcessingException {
        return ok(propertyService.getFileCriteriaData(service, outbound));
    }

}
