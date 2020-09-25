package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.TransmittalException;
import com.ibm.sterling.bfg.app.model.entity.Transmittal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class TransmittalService {

    @Value("${transmittal.url}")
    private String transmittalUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> transmit(Transmittal transmittal) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        transmittal.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String transmittalResponse = null;
        try {
            transmittalResponse = new RestTemplate().postForObject(
                    transmittalUrl,
                    new HttpEntity<>(transmittal, headers),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            Optional.ofNullable(e.getMessage()).ifPresent(errorMessage -> {
                List<Map<String, Object>> errorList = null;
                Matcher matcher = Pattern.compile("\\{.*}").matcher(errorMessage);
                if (matcher.find()) {
                    String errorMessageList = "[" + matcher.group(0) + "]";
                    try {
                        errorList = new ObjectMapper().readValue(errorMessageList, new TypeReference<List<Map<String, Object>>>() {
                        });
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                }
                Map<String, List<Object>> errorMap = Optional.ofNullable(errorList).map(errors -> {
                    Map<String, List<Object>> transmittalErrors = new HashMap<>();
                    errors.forEach(error -> {
                        String key = String.valueOf(error.get("attribute"));
                        if (transmittalErrors.containsKey(key))
                            transmittalErrors.get(key).add(error.get("message"));
                        else
                            transmittalErrors.put(key, new ArrayList<>(Collections.singletonList(error.get("message"))));
                    });
                    return transmittalErrors;
                }).orElseGet(() -> Collections.singletonMap("error", Collections.singletonList(errorMessage)));
                throw new TransmittalException(errorMap, e.getStatusCode());
            });
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(transmittalResponse));
        return objectMapper.convertValue(root, Map.class);
    }

}
