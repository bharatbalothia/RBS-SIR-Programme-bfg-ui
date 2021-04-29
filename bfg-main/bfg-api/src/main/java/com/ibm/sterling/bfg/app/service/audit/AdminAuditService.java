package com.ibm.sterling.bfg.app.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.audit.InvalidEventException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.event.AuditEvent;
import com.ibm.sterling.bfg.app.model.event.AuditEventCriteria;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminAuditService {

    @Value("${adminAudit.url}")
    private String adminAuditUrl;

    @Value("${news.url}")
    private String newsUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyService propertyService;

    public void fireAdminAuditEvent(AdminAuditEventRequest adminAuditEventRequest) {
        try {
            new RestTemplate().postForObject(
                    adminAuditUrl,
                    new HttpEntity<>(adminAuditEventRequest, apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            throw new InvalidEventException(apiDetailsHandler.processErrorMessage(e), e.getStatusCode());
        }
    }

    public List<AuditEvent> getAuditEvents(AuditEventCriteria auditEventCriteria) {
        if (auditEventCriteria.getEventType() == null) {
            auditEventCriteria.setEventType(propertyService.getEventTypesForUser());
        }
        MultiValueMap<String, String> auditCriteriaMultiValueMap = new LinkedMultiValueMap<>();
        objectMapper.convertValue(auditEventCriteria, new TypeReference<Map<String, String>>() {
        }).forEach(auditCriteriaMultiValueMap::add);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(newsUrl)
                .queryParams(auditCriteriaMultiValueMap);
        ResponseEntity<String> response = new RestTemplate().exchange(
                uriBuilder.build().toString(),
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class);
        try {
            return objectMapper.convertValue(objectMapper.readTree(Objects.requireNonNull(response.getBody())),
                    new TypeReference<List<AuditEvent>>() {
                    });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
