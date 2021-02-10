package com.ibm.sterling.bfg.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/version")
public class VersionController {

    @Autowired
    BuildProperties buildProperties;

    @GetMapping
    public ResponseEntity<String> getApplicationVersion() {
        return ok(buildProperties.getVersion());
    }

}
