package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CertificateValidationService {

    private static final Logger LOG = LogManager.getLogger(CertificateValidationService.class);

    @Value("${certificate.validation.url}")
    private String certificateValidationUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> getCertificateChain(String certificateContent) throws JsonProcessingException {
        String certificateValidationResponse;
        try {
            certificateValidationResponse = new RestTemplate().postForObject(
                    certificateValidationUrl,
                    new HttpEntity<>(
                            Collections.singletonMap("certificateBody", certificateContent),
                            apiDetailsHandler.getHttpHeaders(userName, password)
                    ),
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
                            LOG.error("JsonProcessingException in getCertificateChain of CertificateValidationService: " + ex.getMessage());
                        }
                        return Collections.singletonMap("errorMessage", (Object) errorMap);
                    })
                    .orElse(Collections.singletonMap("errorMessage", ""));
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(certificateValidationResponse));
        return objectMapper.convertValue(root, Map.class);
    }

}
