package com.ibm.sterling.bfg.app.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.security.Login;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    private static final Logger LOGGER = LogManager.getLogger(PermissionsService.class);

    @Value("${permissions.url}")
    private String permissionsUrl;

    @Value("${groups.url}")
    private String groupsUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
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

    public List<String> getPermissionList(Login loginRequest) throws JsonProcessingException {
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
                    groupSet.removeIf(isNotNeededAuthorityPredicate(userAccountGroups));
                    groupSet.forEach(group -> permissionSet.addAll(permissions.get(group)));
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
                LOGGER.error("Error reading JSON: {}", e.getOriginalMessage());
            }
        });
        return permissions;
    }

    public Set<String> getPermissionsOfGroup(String group, boolean isChild, List<String> listBFGUIPermissions) throws JsonProcessingException {
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(
                    groupsUrl + "?_range=0-999&searchFor=" + group,
                    HttpMethod.GET,
                    new HttpEntity<>(getHttpHeaders()),
                    String.class);

        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        List<Map<String, String>> authorityList = Optional.ofNullable(objectMapper.convertValue(root, List.class))
                .orElseGet(ArrayList::new);

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
                                        LOGGER.error("Error reading JSON: {}", e.getOriginalMessage());
                                    }
                                });
                            }
                            permissionSet.removeIf(isNotNeededAuthorityPredicate(listBFGUIPermissions));
                            return permissionSet.stream();
                        }
                ).collect(Collectors.toSet());
        } catch (HttpClientErrorException ex) {
            return new HashSet<>();
        }
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
