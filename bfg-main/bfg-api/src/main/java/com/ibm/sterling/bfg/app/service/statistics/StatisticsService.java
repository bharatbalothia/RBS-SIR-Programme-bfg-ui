package com.ibm.sterling.bfg.app.service.statistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.model.statistics.StatisticalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Value("${systemErrorsStatistics.url}")
    private String systemErrorsStatisticsUrl;

    @Value("${sctAlerts.url}")
    private String sctAlertsUrl;

    @Value("${sctTrafficSummary.url}")
    private String sctTrafficSummaryUrl;

    @Value("${sepaTrafficSummary.url}")
    private String sepaTrafficSummaryUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

    public StatisticalData getSystemErrorsStatistics() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                systemErrorsStatisticsUrl,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return objectMapper.convertValue(root, new TypeReference<StatisticalData>() {
        });
    }

    public Map<String, Integer> getSCTAlerts() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                sctAlertsUrl,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        List<StatisticalData> statisticalData = objectMapper.convertValue(root, new TypeReference<List<StatisticalData>>() {
        });
        return statisticalData.stream()
                .collect(Collectors.toMap(StatisticalData::getType, StatisticalData::getCount));
    }

    public Map<String, Map<String, Integer>> getSCTTrafficSummary() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                sctTrafficSummaryUrl,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        List<StatisticalData> statisticalData = objectMapper.convertValue(root, new TypeReference<List<StatisticalData>>() {
        });
        return statisticalData.stream()
                .flatMap(data -> {
                    String type = data.getType();
                    if (type.chars().filter(ch -> ch == '_').count() == 2) {
                        type = type.concat("_TOTAL");
                    }
                    int indexOfLastUnderscoreInType = type.lastIndexOf("_");
                    return Collections.singletonMap(
                            type.substring(0, indexOfLastUnderscoreInType),
                            Collections.singletonMap(type.substring(indexOfLastUnderscoreInType + 1), data.getCount())
                    ).entrySet().stream();
                })
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.toMap(
                                        entry -> entry.getValue().entrySet().iterator().next().getKey(),
                                        entry -> entry.getValue().entrySet().iterator().next().getValue()
                                )
                        )
                );
    }

    public Map<String, Integer> getSepaTrafficSummary() throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                sepaTrafficSummaryUrl,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        List<StatisticalData> statisticalData = objectMapper.convertValue(root, new TypeReference<List<StatisticalData>>() {
        });
        return statisticalData
                .stream()
                .filter(stat -> stat.getType().endsWith("_HOUR"))
                .collect(Collectors.toMap(
                        data -> {
                            String type = data.getType();
                            return type.substring(0, type.lastIndexOf('_'));
                        },
                        StatisticalData::getCount));
    }
}
