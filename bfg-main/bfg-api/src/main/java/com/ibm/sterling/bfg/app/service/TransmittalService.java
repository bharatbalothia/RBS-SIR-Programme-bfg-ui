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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpStatusCodeException;
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
        String transmittalResponse = null;
        try {
            transmittalResponse = new RestTemplate().postForObject(
                    transmittalUrl,
                    new HttpEntity<>(transmittal, new HttpHeaders()),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            Optional.ofNullable(e.getMessage()).ifPresent(errorMessage -> {
                String parsedErrorMessageList = "[" + errorMessage.replaceAll("(^[\\w:\\[\\s]+)|([]\\s]+$)", "") + "]";
                List<Map<String, Object>> errors = null;
                try {
                    errors = new ObjectMapper().readValue(parsedErrorMessageList, new TypeReference<List<Map<String, Object>>>() {
                    });
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
                if (CollectionUtils.isEmpty(errors)) {
                    errors = Collections.singletonList(new HashMap<String, Object>() {{
                        put("attribute", "error");
                        put("message", errorMessage);
                    }});
                }
                Map<String, List<Object>> transmittalErrors = new HashMap<>();
                errors.forEach(error -> {
                    String key = String.valueOf(error.get("attribute"));
                    if (transmittalErrors.containsKey(key))
                        transmittalErrors.get(key).add(error.get("message"));
                    else
                        transmittalErrors.put(key, new ArrayList<>(Collections.singletonList(error.get("message"))));
                });
                throw new TransmittalException(transmittalErrors, e.getStatusCode());
            });
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(transmittalResponse));
        return objectMapper.convertValue(root, Map.class);
    }

}
