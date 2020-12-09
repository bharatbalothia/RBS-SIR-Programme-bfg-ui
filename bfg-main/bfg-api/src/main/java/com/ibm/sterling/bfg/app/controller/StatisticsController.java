package com.ibm.sterling.bfg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.model.statistics.StatisticalData;
import com.ibm.sterling.bfg.app.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/statistics")
@PreAuthorize("hasAuthority('SFG_UI_HOME')")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("system-errors")
    public ResponseEntity<StatisticalData> getSystemErrorsStatistics() throws JsonProcessingException {
        return ok(statisticsService.getSystemErrorsStatistics());
    }

    @GetMapping("sct-alerts")
    public ResponseEntity<Map<String, Integer>> getSCTAlerts() throws JsonProcessingException {
        return ok(statisticsService.getSCTAlerts());
    }

    @GetMapping("sct-traffic")
    public ResponseEntity<Map<String, Map<String, Integer>>> getSCTTrafficSummary() throws JsonProcessingException {
        return ok(statisticsService.getSCTTrafficSummary());
    }

}
