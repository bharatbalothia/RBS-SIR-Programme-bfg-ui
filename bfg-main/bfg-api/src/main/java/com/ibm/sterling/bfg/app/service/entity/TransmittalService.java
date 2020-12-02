package com.ibm.sterling.bfg.app.service.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.exception.entity.TransmittalException;
import com.ibm.sterling.bfg.app.model.entity.Transmittal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TransmittalService {

    @Value("${transmittal.url}")
    private String transmittalUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> transmit(Transmittal transmittal) throws JsonProcessingException {
        transmittal.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String transmittalResponse;
        try {
            transmittalResponse = new RestTemplate().postForObject(
                    transmittalUrl,
                    new HttpEntity<>(transmittal, apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            throw new TransmittalException(apiDetailsHandler.processErrorMessage(e), e.getStatusCode());
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(transmittalResponse));
        return objectMapper.convertValue(root, Map.class);
    }

}
