package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.file.Transaction;
import com.ibm.sterling.bfg.app.model.file.TransactionSearchCriteria;
import com.ibm.sterling.bfg.app.service.PropertyService;
import com.ibm.sterling.bfg.app.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/transactions")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
public class SCTTransactionSearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public Page<Transaction> getSCTTransactions(@Valid @RequestBody(required = false) TransactionSearchCriteria transactionSearchCriteria)
            throws JsonProcessingException {
        return searchService.getSCTTransactionList(
                Optional.ofNullable(transactionSearchCriteria).orElse(new TransactionSearchCriteria()));
    }

    @GetMapping("transaction-criteria-data")
    public ResponseEntity<Map<String, List<Object>>> getFileCriteriaData(
            @RequestParam(value = "direction", required = false) String direction) throws JsonProcessingException {
        return ok(propertyService.getTransactionCriteriaData(direction));
    }

}
