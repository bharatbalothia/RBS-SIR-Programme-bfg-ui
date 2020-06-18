package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class PermissionsService {

    @Value("${permissions.url}")
    private String permissionsUrl;

    @Value("${permissions.userName}")
    private String userName;

    @Value("${permissions.password}")
    private String password;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getPermissionList(LoginRequest loginRequest) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(permissionsUrl)
                .queryParam("_exclude", "preferredLanguage")
                .queryParam("_range", "0-999")
                .queryParam("searchFor", loginRequest.getLogin());

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                new RestTemplate().exchange(
                        uriBuilder.toUriString(),
                        HttpMethod.GET,
                        request,
                        String.class
                );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
        List<Map<String, String>> authorityList = objectMapper.convertValue(root, List.class);
        return authorityList.stream()
                .flatMap(authorities -> {
                            List<Map<String, String>> permissionMap =
                                    objectMapper.convertValue(authorities.get("permissions"), List.class);
                            List<String> permissionList = permissionMap.stream()
                                    .map(permission -> permission.get("name"))
                                    .collect(Collectors.toList());
                            return permissionList.stream();
                        }
                ).collect(Collectors.toList());
    }

}
