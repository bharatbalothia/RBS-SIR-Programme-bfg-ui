package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        Function<String, String> queryStringToGetDataByKey = attributeValue ->
                "?_where=con(" + PROPERTY_KEY + "," + attributeValue + ")";
        Arrays.asList(settings.getFileSearchPostfixKey())
                .forEach(value -> {
                    try {
                        fileCriteriaData.put(value, getPropertyList(settings.getFileUrl() +
                                queryStringToGetDataByKey.apply(
                                        settings.getFileSearchPrefixKey() + value)
                        ).stream()
                                .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE).split(",")))
                                .collect(Collectors.toList()));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        fileCriteriaData.put("type",
                getPropertyList(settings.getFileUrl() +
                        queryStringToGetDataByKey.apply(
                                settings.getFileSearchPrefixKey() + "types." + Optional.ofNullable(service).orElse(""))
                ).stream()
                        .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE).split(",")))
                        .collect(Collectors.toList()));
        fileCriteriaData.put("fileStatus",
                getPropertyList(settings.getFileUrl() +
                        queryStringToGetDataByKey.apply(
                                Optional.ofNullable(service).orElse("") + settings.getFileStatusPrefixKey() +
                                        Optional.ofNullable(outbound).map(bound -> bound ? "outbound" : "inbound").orElse(""))
                ).stream()
                        .map(property -> {
                                    Map<String, Object> fileStatusMap = new HashMap<>();
                                    String propertyKey = property.get(PROPERTY_KEY);
                                    fileStatusMap.put("service", propertyKey.substring(0, propertyKey.indexOf(".")).toUpperCase());
                                    int indexOfLastDotInKey = propertyKey.lastIndexOf(".");
                                    fileStatusMap.put("outbound", "outbound".equals(propertyKey.substring(
                                            propertyKey.lastIndexOf(".", indexOfLastDotInKey - 1) + 1,
                                            indexOfLastDotInKey)));
                                    String noStatusLabel = property.get(PROPERTY_VALUE);
                                    fileStatusMap.put("title", noStatusLabel.replaceAll("\\s*\\(.*\\)", ""));
                                    String status = propertyKey.substring(propertyKey.lastIndexOf(".") + 1);
                                    fileStatusMap.put("status", status);
                                    fileStatusMap.put("label", "[" + status + "] " + noStatusLabel);
                                    return fileStatusMap;
                                }
                        ).collect(Collectors.toList()));
        return fileCriteriaData;
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
