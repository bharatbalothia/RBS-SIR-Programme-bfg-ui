package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CertificateValidationService {

    @Value("${certificate.validation.url}")
    private String certificateValidationUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> getCertificateChain(String certificateContent) throws JsonProcessingException {
        String certificateValidationResponse;
        try {
            certificateValidationResponse = new RestTemplate().postForObject(
                    certificateValidationUrl,
                    new HttpEntity<>(Collections.singletonMap("certificateBody", certificateContent), new HttpHeaders()),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            return Optional.ofNullable(e.getMessage())
                    .map(errorMessage -> {
                        String errorMap = errorMessage.substring(errorMessage.indexOf("[") + 1, errorMessage.indexOf("]"));
                        try {
                            return new ObjectMapper().readValue(errorMap, new TypeReference<Map<String, Object>>() {
                            });
                        } catch (JsonProcessingException ex) {
                            ex.printStackTrace();
                        }
                        return Collections.singletonMap("error", (Object) errorMap);
                    })
                    .orElse(new HashMap<>());
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(certificateValidationResponse));
        return objectMapper.convertValue(root, Map.class);
    }

}
