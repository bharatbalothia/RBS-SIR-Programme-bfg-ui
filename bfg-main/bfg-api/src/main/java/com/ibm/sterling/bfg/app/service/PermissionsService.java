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

        List<Map<String, String>> authorityList = objectMapper.convertValue(root, List.class);

        List<String> userAccountPermissions = propertyService.getUserAccountPermissions();
        List<String> userAccountGroups = propertyService.getUserAccountGroups();
        Map<String, Set<String>> permissions = permissionsService.getPermissionsOfBFGUIGroups(userAccountGroups, userAccountPermissions);

        return Optional.ofNullable(authorityList).map(authList -> authList.stream().flatMap(auth -> {
                            Set<String> permissionSet = getAuthoritySet.apply(auth, "permissions");
                            Set<String> groupSet = getAuthoritySet.apply(auth, "groups");
                            if (!groupSet.isEmpty()) {
                                groupSet.removeIf(isNotNeededPermsPredicate(userAccountGroups));
                                groupSet.forEach(group -> permissionSet.addAll(permissions.get(group)));
                            }
                            permissionSet.removeIf(isNotNeededPermsPredicate(userAccountPermissions));
                            return permissionSet.stream();
                        }
                ).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Cacheable(cacheNames = CACHE_PERMISSIONS)
    public Map<String, Set<String>> getPermissionsOfBFGUIGroups(List<String> listBFGUIGroups, List<String> listBFGUIPermissions)
            {
        Map<String, Set<String>> permissions = new HashMap<>();
        listBFGUIGroups.forEach(group -> {
            try {
                permissions.put(group, getPermissionsOfGroup(group, false, listBFGUIPermissions));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return permissions;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public Set<String> getPermissionsOfGroup(String group, boolean isChild, List<String> listBFGUIPermissions) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                groupsUrl + "?_range=0-999&searchFor=" + group,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);

        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        List<Map<String, String>> authorityList = objectMapper.convertValue(root, List.class);
        return Optional.ofNullable(authorityList).map(authList -> authList.stream().flatMap(auth -> {
                    Set<String> permissionSet = getAuthoritySet.apply(auth, "permissions");

                    if (!isChild) {
                        Set<String> subgroups = getAuthoritySet.apply(auth, "subgroups");
                        subgroups.forEach(subgroup -> {
                            try {
                                permissionSet.addAll(
                                        getPermissionsOfGroup(subgroup, true, listBFGUIPermissions));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    permissionSet.removeIf(isNotNeededPermsPredicate(listBFGUIPermissions));
                    return permissionSet.stream();
                }
        ).collect(Collectors.toSet())).orElse(new HashSet<>());
    }

    private Predicate<String> isNotNeededPermsPredicate(List<String> userAccountPermissions) {
        return perm -> !userAccountPermissions.contains(perm);
    }

    private BiFunction<Map<String, String>, String ,Set<String>> getAuthoritySet = (authorityMap, name) -> {
        List<Map<String, String>> authorityList =
                objectMapper.convertValue(authorityMap.get(name), List.class);
        return Optional.ofNullable(authorityList)
                .map(authList -> authorityList.stream().map(authority -> authority.get("name")).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    };
}
