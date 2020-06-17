package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;
import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class PermissionsService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, String>> getPermissionList(String permissionUrl,
                                                      LoginRequest loginRequest) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = loginRequest.getLogin() + ":" + loginRequest.getPassword();
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("_exclude", "preferredLanguage");
        urlParams.put("_range", "0-999");
        urlParams.put("searchFor", loginRequest.getLogin());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(permissionUrl)
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
        List<Map<String, String>> list = objectMapper.convertValue(root, List.class);
        return list;
    }
}
