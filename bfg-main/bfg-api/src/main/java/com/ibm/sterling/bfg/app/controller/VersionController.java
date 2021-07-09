package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/version")
public class VersionController {

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<?> getApplicationVersion() {
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("version", buildProperties.getVersion());
        try {
            loginMap.put("loginText", propertyService.getLoginText());
        } catch (JsonProcessingException e) {
            loginMap.put("loginText", "");
        }
        try {
            loginMap.put("sepaDashboardVisibility", propertyService.getSepaDashboardVisibility());
        } catch (JsonProcessingException e) {
            loginMap.put("sepaDashboardVisibility", false);
        }
        return ok(loginMap);
    }

    @GetMapping("f5")
    public ResponseEntity<?> getF5Link() throws JsonProcessingException {
        return ok(propertyService.getLinkF5());
    }

    @GetMapping("sepa-visibility")
    public ResponseEntity<Boolean> isSepaDashboardVisible() {
        try {
            return ok(propertyService.getSepaDashboardVisibility());
        } catch (JsonProcessingException e) {
            return ok(false);
        }
    }
}
