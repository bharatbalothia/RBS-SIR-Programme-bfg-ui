package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.config.APIDetailsHandler;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.file.ErrorDetail;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.*;

@Service
public class PropertyService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertySettings settings;

    @Autowired
    private EntityService entityService;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    public Map<String, String> getInboundRequestType() throws JsonProcessingException {
        String reqTypePrefixKey = settings.getReqTypePrefixKey();
        return getPropertyList(settings.getGplUrl()).stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(reqTypePrefixKey))
                .flatMap(property -> {
                            String propertyKey = property.get(PROPERTY_KEY);
                            String typeKey = propertyKey.substring(propertyKey.indexOf(reqTypePrefixKey) +
                                    reqTypePrefixKey.length());
                            return Collections.singletonMap(typeKey,
                                    property.get(PROPERTY_VALUE) + " (" + typeKey + ")").entrySet().stream();
                        }
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<String> getRestoredInboundRequestType(List<String> inboundRequestTypes) {
        String reqTypePrefixKey = settings.getReqTypePrefixKey();
        List<String> typeKeys = inboundRequestTypes.stream()
                .map(type -> reqTypePrefixKey + type)
                .collect(Collectors.toList());
        try {
            return getPropertiesByPartialKey(typeKeys, settings.getGplUrl()).stream()
                    .map(property -> {
                                String propertyKey = property.get(PROPERTY_KEY);
                                return property.get(PROPERTY_VALUE) +
                                        " (" + propertyKey.substring(propertyKey.indexOf(reqTypePrefixKey) +
                                        reqTypePrefixKey.length()) + ")";
                            }
                    ).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getFileType() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getFileTypeKey());
    }

    public List<String> getUserAccountGroups() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getUseraccountGroupsKey());
    }

    public List<String> getUserAccountPermissions() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getUseraccountPermissionsKey());
    }

    private List<String> getListFromPropertyValueByPropertyKey(String url, String propertyKey) throws JsonProcessingException {
        return getPropertyList(getUrl.apply(url, propertyKey)).stream()
                .flatMap(property -> Arrays.stream(property.get(PROPERTY_VALUE).split(",")))
                .collect(Collectors.toList());
    }

    private BiFunction<String, String, String> getUrl = (url, attributeValue) ->
            new Formatter().format("%s?_where=con(%s,%s)",
                    url, PROPERTY_KEY, attributeValue).toString();

    public Map<String, List<String>> getUserAuthorities() throws JsonProcessingException {
        String userAccountGroupsKey = settings.getUseraccountGroupsKey();
        String userAccountPermissionsKey = settings.getUseraccountPermissionsKey();
        List<String> propertyKeys = new ArrayList<>(Arrays.asList(userAccountGroupsKey, userAccountPermissionsKey));
        List<Map<String, String>> propertyList = getPropertiesByPartialKey(propertyKeys, settings.getBfgUiUrl());
        return new HashMap<String, List<String>>() {
            {
                put(userAccountGroupsKey, getAuthoritiesByKey(userAccountGroupsKey, propertyList));
                put(userAccountPermissionsKey, getAuthoritiesByKey(userAccountPermissionsKey, propertyList));
            }
        };
    }

    private List<String> getAuthoritiesByKey(String userAccountGroupsKey, List<Map<String, String>> propertyList) {
        return propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).equals(userAccountGroupsKey))
                .flatMap(property -> Arrays.stream(property.get(PROPERTY_VALUE).split(",")))
                .collect(Collectors.toList());
    }

    public Map<String, List<Object>> getFileCriteriaData(String service, Boolean outbound) throws JsonProcessingException {
        Map<String, List<Object>> fileCriteriaData = new HashMap<>();
        String[] fileSearchPostfixKey = settings.getFileSearchPostfixKey();
        String fileSearchPrefixKey = settings.getFileSearchPrefixKey();
        String typePropertyKey = fileSearchPrefixKey + "types." + Optional.ofNullable(service).orElse("");
        String statusPropertyKey = Optional.ofNullable(service).map(String::toLowerCase).orElse("") +
                settings.getFileStatusPrefixKey() +
                Optional.ofNullable(outbound).map(bound -> bound ? "outbound" : "inbound").orElse("");

        List<String> propertyKeys = new ArrayList<>(Arrays.asList(typePropertyKey, statusPropertyKey));
        propertyKeys.addAll(Arrays.stream(fileSearchPostfixKey)
                .map(value -> fileSearchPrefixKey + value)
                .collect(Collectors.toList()));
        List<Map<String, String>> propertyList = getPropertiesByPartialKey(propertyKeys, settings.getFileUrl());

        Arrays.stream(fileSearchPostfixKey).forEach(value -> fileCriteriaData.put(value, propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).equals(fileSearchPrefixKey + value))
                .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE).split(",")))
                .collect(Collectors.toList())));
        fileCriteriaData.put("type", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(typePropertyKey))
                .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE).split(",")))
                .collect(Collectors.toList()));
        fileCriteriaData.put("fileStatus", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).contains(statusPropertyKey))
                .map(map -> getStatusLabelData(map, true))
                .sorted(Comparator
                        .comparing(getStatusLabelForComparing("title"),
                                Comparator.naturalOrder())
                        .thenComparing(getStatusLabelForComparing("service"),
                                Comparator.naturalOrder())
                        .thenComparing(getOutboundForComparing(),
                                Comparator.naturalOrder())
                        .thenComparing(getStatusLabelForComparing("status"),
                                Comparator.comparingInt(Integer::parseInt)))
                .collect(Collectors.toList()));
        fileCriteriaData.put("entity",
                entityService.findEntitiesByService(service)
                        .stream()
                        .map(entity -> {
                                    Map<String, Object> entityMap = new HashMap<>();
                                    entityMap.put("entityId", entity.getEntityId());
                                    entityMap.put("entityName", entity.getEntity() + "(" + entity.getService() + ")");
                                    return entityMap;
                                }
                        )
                        .collect(Collectors.toList()));
        return fileCriteriaData;
    }

    private List<Map<String, String>> getPropertiesByPartialKey(List<String> propertyKeys, String url) throws JsonProcessingException {
        Function<String, String> queryStringToGetDataByPartialKey = attributeValue ->
                "con(" + PROPERTY_KEY + "," + attributeValue + ")";
        String multipleQuery = propertyKeys.stream()
                .map(queryStringToGetDataByPartialKey)
                .reduce((firstQuery, secondQuery) -> "or(" + firstQuery + "," + secondQuery + ")")
                .orElse(null);
        return getPropertyList(url + "?_where=" +
                Optional.ofNullable(multipleQuery).orElseGet(() -> queryStringToGetDataByPartialKey.apply(null)));
    }

    public Map<String, List<Object>> getTransactionCriteriaData(String direction) throws JsonProcessingException {
        Map<String, List<Object>> transactionCriteriaData = new HashMap<>();
        String[] transactionSearchPostfixKey = settings.getTransactionSearchPostfixKey();
        String transactionSearchPrefixKey = settings.getTransactionSearchPrefixKey();
        String statusPropertyKey = "sct" + settings.getTransactionStatusPrefixKey() +
                Optional.ofNullable(direction).map(String::toLowerCase).orElse("");

        List<String> propertyKeys = Arrays.stream(transactionSearchPostfixKey)
                .map(value -> transactionSearchPrefixKey + value).collect(Collectors.toList());
        propertyKeys.add(statusPropertyKey);
        List<Map<String, String>> propertyList = getPropertiesByPartialKey(propertyKeys, settings.getFileUrl());

        Arrays.stream(transactionSearchPostfixKey).forEach(value -> transactionCriteriaData.put(value, propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).equals(transactionSearchPrefixKey + value))
                .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE).split(",")))
                .collect(Collectors.toList())));

        transactionCriteriaData.put("trxStatus", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(statusPropertyKey))
                .map(map -> getStatusLabelData(map, false))
                .sorted(Comparator
                        .comparing(getStatusLabelForComparing("title"),
                                Comparator.naturalOrder())
                        .thenComparing(getOutboundForComparing(),
                                Comparator.naturalOrder())
                        .thenComparing(getStatusLabelForComparing("status"),
                                Comparator.comparingInt(Integer::parseInt)))
                .collect(Collectors.toList()));
        transactionCriteriaData.put("entity", entityService.findEntitiesByService("SCT")
                .stream().map(Entity::getEntity).collect(Collectors.toList()));
        return transactionCriteriaData;
    }

    private Function<Map<String, Object>, String> getOutboundForComparing() {
        return map ->
                Optional.ofNullable(map.get("outbound"))
                        .map(bound -> (boolean) bound ? "Outbound" : "Inbound")
                        .orElse("");
    }

    private Function<Map<String, Object>, String> getStatusLabelForComparing(String status) {
        return map -> String.valueOf(map.get(status));
    }

    private Map<String, Object> getStatusLabelData(Map<String, String> property, boolean isFile) {
        Map<String, Object> statusMap = new HashMap<>();
        String propertyKey = property.get(PROPERTY_KEY);
        statusMap.put("service", propertyKey.substring(0, propertyKey.indexOf(".")).toUpperCase());
        int indexOfLastDotInKey = propertyKey.lastIndexOf(".");
        statusMap.put("outbound", "outbound".equals(propertyKey.substring(
                propertyKey.lastIndexOf(".", indexOfLastDotInKey - 1) + 1,
                indexOfLastDotInKey)));
        String noStatusLabel = property.get(PROPERTY_VALUE);
        statusMap.put("title", noStatusLabel.replaceAll("\\s*\\(.*\\)", ""));
        String status = propertyKey.substring(propertyKey.lastIndexOf(".") + 1);
        statusMap.put("status", status);
        if (isFile) {
            statusMap.put("label", "[" + status + "] " + noStatusLabel);
        } else {
            statusMap.put("label", noStatusLabel + "[" + status + "] ");
        }
        return statusMap;
    }

    public String getStatusLabel(String statusPrefixKey, String service, Boolean outbound, Integer status) {
        try {
            return getPropertyList(settings.getFileUrl() + "?" + PROPERTY_KEY + "=" +
                    service + statusPrefixKey + (outbound ? "outbound" : "inbound") + "." + Math.abs(status)
            ).stream()
                    .map(property -> status + " [" + property.get(PROPERTY_VALUE) + "]")
                    .collect(Collectors.joining(", "));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<ErrorDetail> getErrorDetailsByCode(String errorCode) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("code", errorCode);
        Function<String, String> queryStringToGetDataByKey = attributeValue ->
                "?_where=con(" + PROPERTY_KEY + "," + attributeValue + ")";
        Arrays.asList(settings.getFileErrorPostfixKey())
                .forEach(value -> {
                    try {
                        errorDetails.put(value.toLowerCase(), getPropertyList(settings.getFileUrl() +
                                queryStringToGetDataByKey.apply(
                                        errorCode + "." + value)
                        ).stream()
                                .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE)))
                                .collect(Collectors.toList())
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining("")));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        ErrorDetail errorDetail = objectMapper.convertValue(errorDetails, ErrorDetail.class);
        if (errorDetail.getName().isEmpty() && errorDetail.getDescription().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(errorDetail);
    }

    public Map<String, List<String>> getMQDetails() throws JsonProcessingException {
        String mqPrefixKey = settings.getMqPrefixKey();
        Map<String, List<String>> mqDetailsMap = getPropertyList(settings.getBfgUiUrl()).stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(mqPrefixKey))
                .flatMap(property -> {
                            String propertyKey = property.get(PROPERTY_KEY);
                            return Collections.singletonMap(
                                    propertyKey.substring(propertyKey.indexOf(mqPrefixKey) + mqPrefixKey.length()),
                                    Arrays.asList(property.get(PROPERTY_VALUE).split(","))
                            ).entrySet().stream();
                        }
                ).collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        )
                );
        mqDetailsMap.putAll(
                Collections.singletonMap(CA_DIGITAL_CERTIFICATES, getPropertyList(settings.getCaCertUrl())
                        .stream()
                        .map(property -> property.get(settings.getCertIdKey()))
                        .collect(Collectors.toList())
                )
        );
        mqDetailsMap.putAll(
                Collections.singletonMap(SYSTEM_DIGITAL_CERTIFICATES, getPropertyList(settings.getSysCertUrl())
                        .stream()
                        .map(property -> property.get(settings.getCertIdKey()))
                        .collect(Collectors.toList())
                )
        );
        return mqDetailsMap;
    }

    private List<Map<String, String>> getPropertyList(String propertyUrl) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                propertyUrl,
                HttpMethod.GET,
                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                String.class
        );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
        return objectMapper.convertValue(root, List.class);
    }

}
