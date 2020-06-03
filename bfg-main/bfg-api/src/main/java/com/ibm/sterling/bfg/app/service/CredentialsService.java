package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import com.ibm.sterling.bfg.app.model.security.UserCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredentialsService {

    private static final Logger LOG = LogManager.getLogger(CredentialsService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserCredentials getSBIAuthResponse(LoginRequest loginRequest) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> loginMap = new LinkedMultiValueMap<String, String>() {
            {
                add("userName", loginRequest.getLogin());
                add("password", loginRequest.getPassword());
            }
        };
        String userCredentials = restTemplate.postForObject(
                "https://b2bi-int1.fyre.ibm.com:25060/restwar/restapi/v1.0/authenticate/",
                new HttpEntity<>(loginMap, headers),
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(userCredentials));
        JsonNode user = root.get("user");
        return Optional.ofNullable(user.get("authenticated")).map(JsonNode::asBoolean).map(auth -> {
                    List<String> groups = objectMapper.convertValue(user.get("groups"), ArrayList.class);
                    return new UserCredentials(
                            user.get("name").asText(),
                            null,
                            groups.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList())
                    );
                }
        ).orElseThrow(() -> new BadCredentialsException("Authentication failed"));
    }

}
