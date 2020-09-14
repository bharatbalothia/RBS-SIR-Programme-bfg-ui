package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.entity.Transmittal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TransmittalService {

    @Value("${transmittal.url}")
    private String transmittalUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> transmit(Transmittal transmittal) throws JsonProcessingException {
        transmittal.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String transmittalResponse = new RestTemplate().postForObject(
                transmittalUrl,
                new HttpEntity<>(transmittal, new HttpHeaders()),
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(transmittalResponse));
        return objectMapper.convertValue(root, Map.class);
    }

}
