package com.ibm.sterling.bfg.app.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.security.AuthenticationFailedException;
import com.ibm.sterling.bfg.app.model.security.Login;
import com.ibm.sterling.bfg.app.model.security.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
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
        JsonNode verifiedUser = Optional.ofNullable(getVerifiedUser(loginRequest))
                .orElseThrow(() -> new AuthenticationFailedException("Authentication failed. Invalid Username or Password."));
        List<String> permissionList = permissionsService.getPermissionList(loginRequest);
        return new UserCredentials(
                Optional.ofNullable(verifiedUser.get("name")).map(JsonNode::asText).orElse("No Name"),
                null,
                permissionList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }

    public boolean verifyIdentity(Login loginRequest) throws JsonProcessingException {
        return Optional.ofNullable(getVerifiedUser(loginRequest)).isPresent();
    }

    private JsonNode getVerifiedUser(Login loginRequest) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String userCredentials = new RestTemplate().postForObject(
                authenticationUrl + loginRequest.urlPostfix(),
                new HttpEntity<>(loginRequest.retrieveFields(), headers),
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(userCredentials));
        return Optional.ofNullable(root.get("user"))
                .filter(userNode -> Optional.ofNullable(userNode.get("authenticated"))
                        .map(JsonNode::asBoolean)
                        .orElse(false))
                .orElse(null);
    }

}
