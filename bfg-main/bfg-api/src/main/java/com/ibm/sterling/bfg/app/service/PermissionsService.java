package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.config.cache.CacheSpec.CACHE_PERMISSIONS;
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

    @Value("${property.useraccountGroupsKey}")
    private String userAccountGroupsKey;

    @Value("${property.useraccountPermissionsKey}")
    private String userAccountPermissionsKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PermissionsService permissionsService;

    public List<String> getPermissionList(LoginRequest loginRequest) throws JsonProcessingException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(permissionsUrl)
                .queryParam("_exclude", "preferredLanguage")
                .queryParam("_range", "0-999")
                .queryParam("searchFor", loginRequest.getLogin());

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
        List<Map<String, String>> authorityList = Optional.ofNullable(objectMapper.convertValue(root, List.class)).orElseGet(ArrayList::new);

        Map<String, List<String>> userAuthorities = propertyService.getUserAuthorities();
        List<String> userAccountGroups = userAuthorities.get(userAccountGroupsKey);
        List<String> userAccountPermissions = userAuthorities.get(userAccountPermissionsKey);
        Map<String, Set<String>> permissions = permissionsService.getPermissionsOfBFGUIGroups(userAccountGroups, userAccountPermissions);

        return authorityList.stream().flatMap(auth -> {
                    Set<String> permissionSet = getAuthoritySet.apply(auth, "permissions");
                    Set<String> groupSet = getAuthoritySet.apply(auth, "groups");
                    if (!groupSet.isEmpty()) {
                        groupSet.removeIf(isNotNeededAuthorityPredicate(userAccountGroups));
                        groupSet.forEach(group -> permissionSet.addAll(permissions.get(group)));
                    }
                    permissionSet.removeIf(isNotNeededAuthorityPredicate(userAccountPermissions));
                    return permissionSet.stream();
                }
        ).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = CACHE_PERMISSIONS)
    public Map<String, Set<String>> getPermissionsOfBFGUIGroups(List<String> listOfBFGUIGroups, List<String> listOfBFGUIPermissions) {
        Map<String, Set<String>> permissions = new HashMap<>();
        listOfBFGUIGroups.forEach(group -> {
            try {
                permissions.put(group, getPermissionsOfGroup(group, false, listOfBFGUIPermissions));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return permissions;
    }

    public Set<String> getPermissionsOfGroup(String group, boolean isChild, List<String> listBFGUIPermissions) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                groupsUrl + "?_range=0-999&searchFor=" + group,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        List<Map<String, String>> authorityList = Optional.ofNullable(objectMapper.convertValue(root, List.class)).orElseGet(ArrayList::new);

        return authorityList.stream()
                .flatMap(authorities -> {
                            Set<String> permissionSet = getAuthoritySet.apply(authorities, "permissions");
                            if (!isChild) {
                                Set<String> subgroups = getAuthoritySet.apply(authorities, "subgroups");
                                subgroups.forEach(subgroup -> {
                                    try {
                                        permissionSet.addAll(
                                                getPermissionsOfGroup(subgroup, true, listBFGUIPermissions));
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }

                                });
                            }
                            permissionSet.removeIf(isNotNeededAuthorityPredicate(listBFGUIPermissions));
                            return permissionSet.stream();
                        }
                ).collect(Collectors.toSet());
    }

    private Predicate<String> isNotNeededAuthorityPredicate(List<String> userAccountAuthorities) {
        return authority -> !userAccountAuthorities.contains(authority);
    }

    private BiFunction<Map<String, String>, String, Set<String>> getAuthoritySet = (authorityMap, name) -> {
        List<Map<String, String>> authorityList = objectMapper.convertValue(authorityMap.get(name), List.class);
        return Optional.ofNullable(authorityList)
                .map(authList -> authorityList.stream().map(authority -> authority.get("name")).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    };

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
