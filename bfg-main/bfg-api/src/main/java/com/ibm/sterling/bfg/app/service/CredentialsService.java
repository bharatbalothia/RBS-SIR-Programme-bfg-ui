package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.Login;
import com.ibm.sterling.bfg.app.model.security.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredentialsService {

    @Value("${authentication.url}")
    private String authenticationUrl;

    @Autowired
    private PermissionsService permissionsService;

    @Autowired
    private ObjectMapper objectMapper;

    public UserCredentials getSBIAuthResponse(Login loginRequest) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> loginMap = loginRequest.retrieveFields();

        String userCredentials = restTemplate.postForObject(
                authenticationUrl + loginRequest.urlPostfix(),
                new HttpEntity<>(loginMap, headers),
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(userCredentials));
        JsonNode user = root.get("user");
        JsonNode error = root.get("error");
        List<String> permissionList = permissionsService.getPermissionList(loginRequest);

        return Optional.ofNullable(user.get("authenticated"))
                .filter(JsonNode::asBoolean).map(auth -> new UserCredentials(
                        user.get("name").asText(),
                        null,
                        permissionList.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                )).orElseThrow(() -> new BadCredentialsException("Authentication failed"));
    }
}
