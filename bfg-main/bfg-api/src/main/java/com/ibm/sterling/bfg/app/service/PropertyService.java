package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Value("${property.userName}")
    private String userName;

    @Value("${property.password}")
    private String password;

    @Value("${property.reqTypePrefixKey}")
    private String propertyPrefixKey;

    @Value("${property.fileTypeKey}")
    private String fileTypeKey;

    private static final String PROPERTY_KEY = "propertyKey";
    private static final String PROPERTY_VALUE = "propertyValue";
    private static final String HEADER_PREFIX = "Basic ";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getInboundRequestType() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        "http://b2bi-int1.fyre.ibm.com:25074/B2BAPIs/svc/propertyfiles/GPL/property/",
                        HttpMethod.GET,
                        request,
                        String.class
                );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
        List<Map<String, String>> propertyList = objectMapper.convertValue(root, List.class);
        return propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(propertyPrefixKey))
                .map(property -> {
                            String propertyKey = property.get(PROPERTY_KEY);
                            return property.get(PROPERTY_VALUE) +
                                    " (" + propertyKey.substring(propertyKey.indexOf(propertyPrefixKey) +
                                    propertyPrefixKey.length()) + ")";
                        }
                ).collect(Collectors.toList());
    }

    public List<String> getFileType() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        "http://b2bi-int1.fyre.ibm.com:25074/B2BAPIs/svc/propertyfiles/bfgui/property/",
                        HttpMethod.GET,
                        request,
                        String.class
                );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
        List<Map<String, String>> propertyList = objectMapper.convertValue(root, List.class);
        return propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).equals(fileTypeKey))
                .map(property -> Arrays.asList(property.get(PROPERTY_VALUE).split(",")))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
