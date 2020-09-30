package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.file.ErrorDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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

    public List<String> getInboundRequestType() throws JsonProcessingException {
        String reqTypePrefixKey = settings.getReqTypePrefixKey();
        return getPropertyList(settings.getGplUrl()).stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(reqTypePrefixKey))
                .map(property -> {
                            String propertyKey = property.get(PROPERTY_KEY);
                            return property.get(PROPERTY_VALUE) +
                                    " (" + propertyKey.substring(propertyKey.indexOf(reqTypePrefixKey) +
                                    reqTypePrefixKey.length()) + ")";
                        }
                ).collect(Collectors.toList());
    }

    public List<String> getFileType() throws JsonProcessingException {
        return getPropertyList(settings.getBfgUiUrl()).stream()
                .filter(property -> property.get(PROPERTY_KEY).equals(settings.getFileTypeKey()))
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
                .map(this::getStatusLabelData)
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
        return getPropertyList(url + Optional.ofNullable(multipleQuery).map(query -> "?_where=" + query).orElse(null));
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
                .map(this::getStatusLabelData)
                .collect(Collectors.toList()));
        transactionCriteriaData.put("entity", entityService.findEntitiesByService("SCT")
                .stream().map(Entity::getEntity).collect(Collectors.toList()));
        return transactionCriteriaData;
    }

    private Map<String, Object> getStatusLabelData(Map<String, String> property) {
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
        statusMap.put("label", "[" + status + "] " + noStatusLabel);
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
                                .map(val -> String.valueOf(val))
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
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = settings.getUserName() + ":" + settings.getPassword();
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                new RestTemplate().exchange(
                        propertyUrl,
                        HttpMethod.GET,
                        request,
                        String.class
                );
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
        return objectMapper.convertValue(root, List.class);
    }

}

