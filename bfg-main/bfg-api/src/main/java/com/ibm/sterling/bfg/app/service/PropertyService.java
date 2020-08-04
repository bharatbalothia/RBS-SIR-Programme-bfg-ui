package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, List<String>> getFileCriteriaData() throws JsonProcessingException {
        Map<String, List<String>> fileCriteriaData = new HashMap<>();
        fileCriteriaData.put("type", getPropertyList(
                settings.getFileUrl() + "?" + PROPERTY_KEY + "=" + settings.getFileTypePrefixListKey())
                .stream()
                .flatMap(property -> Arrays.stream(property.get(PROPERTY_VALUE).split(",")))
                .map(fileTypeKeyPostfix -> {
                            try {
                                return getPropertyList(settings.getFileUrl() + "?" +
                                        PROPERTY_KEY + "=" + settings.getFileTypePrefixKey() + fileTypeKeyPostfix)
                                        .get(0).get(PROPERTY_VALUE);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                ).collect(Collectors.toList()));
        fileCriteriaData.put("fileStatus",
                getPropertyList(settings.getFileUrl() + "?_where=con(" + PROPERTY_KEY + "," +
                        settings.getFileStatusPrefixKey() + ")").stream()
                        .map(property -> {
                                    String propertyKey = property.get(PROPERTY_KEY);
                                    return "[" + propertyKey.substring(propertyKey.lastIndexOf(".") + 1) + "] " +
                                            property.get(PROPERTY_VALUE);
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
