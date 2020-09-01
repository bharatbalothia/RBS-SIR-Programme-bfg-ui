package com.ibm.sterling.bfg.app.controller;

import com.ibm.sterling.bfg.app.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/transactions")
public class SCTTransactionSearchController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("transaction-criteria-data")
    public ResponseEntity<Map<String, List<Object>>> getFileCriteriaData(
            @RequestParam(value = "direction", required = false) String direction) {
        return ok(propertyService.getTransactionCriteriaData(direction));
    }

}
