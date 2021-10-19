package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.file.ErrorDetailsNotFoundException;
import com.ibm.sterling.bfg.app.exception.file.FileNotFoundException;
import com.ibm.sterling.bfg.app.model.file.*;
import com.ibm.sterling.bfg.app.model.report.ReportType;
import com.ibm.sterling.bfg.app.service.PropertyService;
import com.ibm.sterling.bfg.app.service.file.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/files")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
public class FileSearchController {

    @Autowired
    private SearchService fileSearchService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public Page<File> getFiles(@Valid @RequestBody(required = false) FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        return fileSearchService.getFilesList(Optional.ofNullable(fileSearchCriteria).orElse(new FileSearchCriteria()),
                File.class);
    }

    @PostMapping("sepa")
    public Page<SEPAFile> getSEPAFiles(@Valid @RequestBody(required = false) FileSearchCriteria fileSearchCriteria) throws JsonProcessingException {
        return fileSearchService.getFilesList(Optional.ofNullable(fileSearchCriteria).orElse(new FileSearchCriteria()),
                SEPAFile.class);
    }

    @GetMapping("{id}")
    public ResponseEntity<File> getFileById(@PathVariable Integer id) throws JsonProcessingException {
        return ok(fileSearchService.getFileById(id).orElseThrow(FileNotFoundException::new));
    }

    @GetMapping("{fileId}/transactions")
    public Page<Transaction> getTransactionsForFiles(@PathVariable Integer fileId,
                                                     @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false) Integer size)
            throws JsonProcessingException {
        return fileSearchService.getTransactionsList(fileId, page, size);
    }

    @GetMapping("error/{errorCode}")
    public ResponseEntity<ErrorDetail> getErrorDetailsByCode(@PathVariable String errorCode) throws JsonProcessingException {
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
    public ResponseEntity<Map<String, String>> getDocumentContent(
            @RequestParam(value = "id", required = false) String documentId,
            @RequestParam(value = "messageId", required = false) Integer messageId) {
        Map<String, String> emptyMap = Collections.singletonMap("document", null);
        return Optional.ofNullable(documentId).map(docId -> {
            try {
                return ok(fileSearchService.getDocumentPayload(documentId));
            } catch (JsonProcessingException e) {
                return ok(emptyMap);
            }
        }).orElse(ok(fileSearchService.getDocumentPayloadByMessageId(messageId)));
    }

    @GetMapping("file-monitor")
    public ResponseEntity<List<File>> getFileMonitor() throws JsonProcessingException {
        return ok(fileSearchService.getFileMonitor().orElseThrow(FileNotFoundException::new));
    }

    @GetMapping("sepa/export-report")
    public ResponseEntity<InputStreamResource> exportFilesReport(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam String type) throws IOException {
        ReportType reportType = new ObjectMapper().convertValue(type, ReportType.class);
        ByteArrayInputStream in = fileSearchService.generateReportForSEPAFiles(from, to, reportType);
        HttpHeaders headers = getHttpHeadersForReport(reportType, "SEPA_FILES_");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(reportType.mediaType())
                .body(new InputStreamResource(in));
    }

    private HttpHeaders getHttpHeadersForReport(ReportType reportType, String fileNamePrefix) {
        LocalDateTime currentDate = LocalDateTime.now();
        String ddMMyy = currentDate.format(DateTimeFormatter.ofPattern("ddMMyyHHmm"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename="+ fileNamePrefix + ddMMyy + reportType.extension());
        return headers;
    }

    @GetMapping("{fileId}/transactions/export-report")
    public ResponseEntity<InputStreamResource> exportTransactionsReport
            (@PathVariable Integer fileId,
             @RequestParam String fileName,
             @RequestParam(value = "size", defaultValue = "100000", required = false) Integer size,
             @RequestParam String type)
            throws IOException {
        ReportType reportType = new ObjectMapper().convertValue(type, ReportType.class);
        ByteArrayInputStream in = fileSearchService.generateReportForTransactions(fileId, fileName, size, reportType);
        HttpHeaders headers = getHttpHeadersForReport(reportType, "Transactions_for_" + fileId + "_");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(reportType.mediaType())
                .body(new InputStreamResource(in));
    }
}
