package com.ibm.sterling.bfg.app.service.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.exception.audit.InvalidEventException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminAuditService {

    @Value("${adminAudit.url}")
    private String adminAuditUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

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

}
