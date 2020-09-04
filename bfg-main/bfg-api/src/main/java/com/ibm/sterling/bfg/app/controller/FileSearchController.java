package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.exception.ErrorDetailsNotFoundException;
import com.ibm.sterling.bfg.app.exception.FileNotFoundException;
import com.ibm.sterling.bfg.app.exception.FileTransactionNotFoundException;
import com.ibm.sterling.bfg.app.model.file.ErrorDetail;
import com.ibm.sterling.bfg.app.model.file.File;
import com.ibm.sterling.bfg.app.model.file.FileSearchCriteria;
import com.ibm.sterling.bfg.app.model.file.Transaction;
import com.ibm.sterling.bfg.app.service.SearchService;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/files")
public class FileSearchController {

    @Autowired
    private SearchService fileSearchService;

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
    public Page<Transaction> getTransactionsForFiles(
            @PathVariable Integer fileId,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Min(1) Integer size,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page)
            throws JsonProcessingException {
        return fileSearchService.getTransactionsList(fileId, size, page);
    }

    @GetMapping("{fileId}/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Integer fileId,
                                                          @PathVariable Integer id) throws JsonProcessingException {
        return ok(fileSearchService.getTransactionById(fileId, id).orElseThrow(FileTransactionNotFoundException::new));
    }

    @GetMapping("error/{errorCode}")
    public ResponseEntity<ErrorDetail> getErrorDetailsByCode(@PathVariable String errorCode) {
        return ok(propertyService.getErrorDetailsByCode(errorCode)
                .orElseThrow(ErrorDetailsNotFoundException::new));
    }

    @GetMapping("file-criteria-data")
    public ResponseEntity<Map<String, List<Object>>> getFileCriteriaData(
            @RequestParam(value = "service", required = false) String service,
            @RequestParam(value = "outbound", required = false) Boolean outbound) throws JsonProcessingException {
        return ok(propertyService.getFileCriteriaData(service, outbound));
    }

    @GetMapping("document-content")
    public ResponseEntity<Map<String, String>> getDocumentContent(@RequestParam(value = "id") String documentId)
            throws JsonProcessingException {
        return ok(fileSearchService.getDocumentContent(documentId.isEmpty() ? null : documentId));
    }

}
