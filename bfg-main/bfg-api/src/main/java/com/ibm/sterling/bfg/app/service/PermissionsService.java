package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class PermissionsService {

    @Value("${permissions.url}")
    private String permissionsUrl;

    @Value("${groups.url}")
    private String groupsUrl;

    @Value("${permissions.userName}")
    private String userName;

    @Value("${permissions.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyService propertyService;

    public List<String> getPermissionList(LoginRequest loginRequest) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        setHttpHeaders(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(permissionsUrl)
                .queryParam("_exclude", "preferredLanguage")
                .queryParam("_range", "0-999")
                .queryParam("searchFor", loginRequest.getLogin());

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));

        List<Map<String, String>> authorityList = objectMapper.convertValue(root, List.class);

        List<String> userAccountPermissions = propertyService.getUserAccountPermissions();
        List<String> userAccountGroups = propertyService.getUserAccountGroups();
        Map<String, Set<String>> permissions = getPermissionsOfBFGUIGroups();

        return authorityList.stream()
                .flatMap(authorities -> {
                            List<Map<String, String>> permissionMap =
                                    objectMapper.convertValue(authorities.get("permissions"), List.class);
                            List<Map<String, String>> groupMap =
                                    objectMapper.convertValue(authorities.get("groups"), List.class);
                            Set<String> permissionSet = permissionMap.stream()
                                    .map(permission -> permission.get("name"))
                                    .collect(Collectors.toSet());
                            Set<String> groupSet = new HashSet<>();
                            Optional.ofNullable(groupMap).ifPresent(map -> {
                                groupSet.addAll(
                                        map.stream()
                                                .map(group -> group.get("name"))
                                                .collect(Collectors.toSet()));
                            });

                            groupSet.removeIf(isContainsNeededPermsPredicate(userAccountGroups));

                            groupSet.forEach(group -> permissionSet.addAll(permissions.get(group)));

                            permissionSet.removeIf(isContainsNeededPermsPredicate(userAccountPermissions));
                            return permissionSet.stream();
                        }
                ).collect(Collectors.toList());
    }

    public Map<String, Set<String>> getPermissionsOfBFGUIGroups() throws JsonProcessingException {
        Map<String, Set<String>> permissions = new HashMap<>();
        propertyService.getUserAccountGroups().forEach(group -> {
            try {
                permissions.put(group, getPermissionsOfGroup(group, false));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return permissions;
    }

    private void setHttpHeaders(HttpHeaders headers) {
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public Set<String> getPermissionsOfGroup(String group, boolean isChild) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        setHttpHeaders(headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
                groupsUrl + "?_range=0-999&searchFor=" + group,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        List<Map<String, String>> authorityList = objectMapper.convertValue(root, List.class);

        List<String> userAccountPermissions = propertyService.getUserAccountPermissions();

        return authorityList.stream()
                .flatMap(authorities -> {
                            List<Map<String, String>> permissionMap =
                                    objectMapper.convertValue(authorities.get("permissions"), List.class);
                            Set<String> permissionSet = permissionMap.stream()
                                    .map(permission -> permission.get("name"))
                                    .collect(Collectors.toSet());
                            if (!isChild) {
                                List<Map<String, String>> subgroupMap = objectMapper.convertValue(authorities.get("subgroups"), List.class);
                                Optional.ofNullable(subgroupMap).ifPresent(map -> {
                                    Set<String> subgroups = subgroupMap.stream()
                                            .map(permission -> permission.get("name"))
                                            .collect(Collectors.toSet());
                                    subgroups.forEach(subgroup -> {
                                        try {
                                            permissionSet.addAll(
                                                    getPermissionsOfGroup(subgroup, true));
                                        } catch (JsonProcessingException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                });
                                permissionSet.removeIf(isContainsNeededPermsPredicate(userAccountPermissions));
                            }
                            return permissionSet.stream();
                        }
                ).collect(Collectors.toSet());
    }

    private Predicate<String> isContainsNeededPermsPredicate(List<String> userAccountPermissions) {
        return perm -> !userAccountPermissions.contains(perm);
    }
}
