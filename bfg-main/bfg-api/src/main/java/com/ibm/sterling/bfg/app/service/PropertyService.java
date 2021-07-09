package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.audit.InvalidUserForEventLogAccessException;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.event.Action;
import com.ibm.sterling.bfg.app.model.event.ActionType;
import com.ibm.sterling.bfg.app.model.event.EventType;
import com.ibm.sterling.bfg.app.model.event.EventTypePermission;
import com.ibm.sterling.bfg.app.model.file.ErrorDetail;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibm.sterling.bfg.app.model.entity.EntityService.SCT;
import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.*;

@Service
public class PropertyService {

    private static final Logger LOGGER = LogManager.getLogger(PropertyService.class);

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

    private static final Integer TOTAL_ROWS_FOR_EXPORT_TRX = 100_000;
    private static final Integer TOTAL_ROWS_FOR_EXPORT_FILE = 100_000;

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
            LOGGER.error("Error reading JSON: {}", e.getOriginalMessage());
        }
        return null;
    }

    public String getInboundService() throws JsonProcessingException {
        return getDefaultService(settings.getInboundServiceKey());
    }

    public String getSwiftService() throws JsonProcessingException {
        return getDefaultService(settings.getSwiftServiceKey());
    }

    private String getDefaultService(String serviceKey) throws JsonProcessingException {
        return getPropertyList(getUrl.apply(settings.getBfgUiUrl(), serviceKey)).stream()
                .map(property -> property.get(PROPERTY_VALUE))
                .findFirst()
                .orElse("");
    }

    public String getTrustedCertsImportSchedule() {
        try {
            return getPropertyList(getUrl.apply(settings.getBfgUiUrl(), settings.getTrustedCertsImportSchedule())).stream()
                    .map(property -> property.get(PROPERTY_VALUE))
                    .findFirst()
                    .orElse("0 0 10-18/2 * * MON-FRI");
        } catch (JsonProcessingException e) {
            return "0 0 10-18/2 * * MON-FRI";
        }
    }

    public List<String> getFileType() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getFileTypeKey());
    }

    public String getLoginText() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getLoginText())
                .stream()
                .findFirst()
                .orElse("");
    }

    public Integer getFileMaxValueForReport() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getSepaDashboardFileMaxValue())
                .stream()
                .findFirst()
                .map(Integer::valueOf)
                .orElse(TOTAL_ROWS_FOR_EXPORT_FILE);
    }

    public Integer getTrxMaxValueForReport() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getSepaDashboardTrxMaxValue())
                .stream()
                .findFirst()
                .map(Integer::valueOf)
                .orElse(TOTAL_ROWS_FOR_EXPORT_TRX);
    }

    public Boolean getSepaDashboardVisibility() throws JsonProcessingException {
        return getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getSepaDashboardVisibility())
                .stream()
                .findFirst()
                .map(Boolean::valueOf)
                .orElse(false);
    }

    public List<Map<String, Object>> getLinkF5() throws JsonProcessingException {
        String property = getListFromPropertyValueByPropertyKey(settings.getBfgUiUrl(), settings.getLinkF5())
                .stream()
                .findFirst()
                .orElse("");
        return Arrays.stream(property.split(";"))
                .collect(Collectors.toList())
                .stream()
                .map(value -> {
                    Map<String, Object> linkMap = new HashMap<>();
                    linkMap.putAll(Arrays.stream(value.split("&&"))
                    .map(kv -> kv.split("=="))
                    .collect(Collectors.toMap(a -> a[0],
                            a -> {
                            if (a[1].equalsIgnoreCase("false")
                                    || a[1].equalsIgnoreCase("true")) {
                                return Boolean.valueOf(a[1]);
                            } else return a[1];
                    })));
                    return linkMap;
                })
                .collect(Collectors.toList());
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
        fileCriteriaData.put("types", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(typePropertyKey))
                .map(property -> {
                    List<Map<String, Object>> types = new ArrayList<>();
                    String propertyKey = property.get(PROPERTY_KEY);
                    String serviceType = propertyKey.substring(propertyKey.lastIndexOf(".") + 1);
                    Arrays.asList(property.get(PROPERTY_VALUE).split(","))
                            .forEach(type -> {
                                Map<String, Object> typeMap = new HashMap<>();
                                typeMap.put("service", serviceType);
                                typeMap.put("type", type);
                                types.add(typeMap);
                            });
                    return types;
                })
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        fileCriteriaData.put("fileStatus", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).contains(statusPropertyKey))
                .map(map -> getStatusLabelData(map, true))
                .sorted(Comparator
                        .comparing(getValueForComparingByKey("title"),
                                Comparator.naturalOrder())
                        .thenComparing(getValueForComparingByKey("service"),
                                Comparator.naturalOrder())
                        .thenComparing(getOutboundForComparing(),
                                Comparator.naturalOrder())
                        .thenComparing(getValueForComparingByKey("status"),
                                Comparator.comparingInt(Integer::parseInt)))
                .collect(Collectors.toList()));
        fileCriteriaData.put("entity",
                entityService.findEntitiesByService(service)
                        .stream()
                        .map(entity -> {
                                    Map<String, Object> entityMap = new HashMap<>();
                                    entityMap.put("entityId", entity.getEntityId());
                                    entityMap.put("entityName", entity.getEntity() + "(" + entity.getService() + ")");
                                    entityMap.put("service", entity.getService());
                                    return entityMap;
                                }
                        )
                        .collect(Collectors.toList()));
        return fileCriteriaData;
    }

    private Map<String, List<Object>> convertTypeProperty(Map<String, String> property) {
        Map<String, List<Object>> typeMap = new HashMap<>();
        String propertyKey = property.get(PROPERTY_KEY);
        String service = propertyKey.substring(propertyKey.lastIndexOf("."));
        typeMap.put(service, Arrays.asList(property.get(PROPERTY_VALUE).split(",")));
        return typeMap;
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

    public Map<String, Object> getEventCriteriaData() {
        Map<String, Object> eventCriteriaData = new HashMap<>();
        eventCriteriaData.put("eventType", getEventTypesForUser());
        eventCriteriaData.put("action", Arrays.asList(Action.values()));
        eventCriteriaData.put("actionType", Arrays.asList(ActionType.values()));
        return eventCriteriaData;
    }

    public List<EventType> getEventTypesForUser() {
        List<String> listOfPermissions =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
        List<EventType> eventTypes = Stream.of(EventTypePermission.values())
                .map(EventTypePermission::getNeededPermission)
                .filter(
                        eventPermission ->
                                listOfPermissions.stream().anyMatch(s -> s.contains(eventPermission)))
                .map(EventTypePermission::convertNeededPermissionToEventType)
                .collect(Collectors.toList());
        if (eventTypes.isEmpty()) {
            throw new InvalidUserForEventLogAccessException();
        }
        return eventTypes;
    }

    public Map<String, List<Object>> getTransactionCriteriaData(String direction) throws JsonProcessingException {
        Map<String, List<Object>> transactionCriteriaData = new HashMap<>();
        String typePropertyKey = settings.getTransactionTypeKey();
        String directionPropertyKey =  settings.getTransactionDirectionPrefixKey();
        String statusPropertyKey = "sct" + settings.getTransactionStatusPrefixKey() +
                Optional.ofNullable(direction).map(String::toLowerCase).orElse("");

        List<String> propertyKeys = new ArrayList<>();
        propertyKeys.add(directionPropertyKey);
        propertyKeys.add(typePropertyKey);
        propertyKeys.add(statusPropertyKey);
        List<Map<String, String>> propertyList = getPropertiesByPartialKey(propertyKeys, settings.getFileUrl());

        transactionCriteriaData.put("type", propertyList.stream()
                .filter(property ->
                    property.get(PROPERTY_KEY).equals(typePropertyKey)
                )
                .flatMap(property -> Stream.of(property.get(PROPERTY_VALUE).split(",")))
                .collect(Collectors.toList()));

        transactionCriteriaData.put("trxStatus", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(statusPropertyKey))
                .map(map -> getStatusLabelData(map, false))
                .sorted(Comparator
                        .comparing(getValueForComparingByKey("title"),
                                Comparator.naturalOrder())
                        .thenComparing(getOutboundForComparing(),
                                Comparator.naturalOrder())
                        .thenComparing(getValueForComparingByKey("status"),
                                Comparator.comparingInt(Integer::parseInt)))
                .collect(Collectors.toList()));

        transactionCriteriaData.put("direction", propertyList.stream()
                .filter(property -> property.get(PROPERTY_KEY).startsWith(directionPropertyKey))
                .map(this::getDirectionLabelData)
                .filter(object -> !object.isEmpty())
                .sorted(Comparator
                        .comparing(getValueForComparingByKey("index"),
                                Comparator.comparingInt(Integer::parseInt)))
                .collect(Collectors.toList()));

        transactionCriteriaData.put("entity", entityService.findEntitiesByService(SCT.name())
                .stream().map(Entity::getEntity).collect(Collectors.toList()));
        return transactionCriteriaData;
    }

    private Function<Map<String, Object>, String> getOutboundForComparing() {
        return map ->
                Optional.ofNullable(map.get("outbound"))
                        .map(bound -> (boolean) bound ? "Outbound" : "Inbound")
                        .orElse("");
    }

    private Function<Map<String, Object>, String> getValueForComparingByKey(String key) {
        return map -> String.valueOf(map.get(key));
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

    private Map<String, Object> getDirectionLabelData(Map<String, String> property) {
        Map<String, Object> directionMap = new HashMap<>();
        String propertyKey = property.get(PROPERTY_KEY);
        final Pattern directionKey = Pattern.compile("^transaction\\.search\\.direction\\.(\\d)[.]([A-Z]\\w+)\\Z");
        Matcher data = directionKey.matcher(propertyKey);
        if(data.matches()) {
            Integer index = Integer.parseInt(data.group(1));
            String  directionLabel = data.group(2);
            directionMap.put("index", index);
            directionMap.put("directionLabel", directionLabel.replace("_", " "));
            directionMap.putAll(Arrays.stream(property.get(PROPERTY_VALUE).split("&"))
                    .map(value -> value.split("="))
                    .collect(Collectors.toMap(a -> a[0], a -> a[1])));
        }
        return directionMap;
    }

    public String getStatusLabel(String statusPrefixKey, String service, String direction, Integer status) {
        try {
            return getPropertyList(settings.getFileUrl() + "?" + PROPERTY_KEY + "=" +
                    service + statusPrefixKey + ("outbound".equals(direction) ? "outbound" : "inbound") + "." + Math.abs(status)
            ).stream()
                    .map(property -> status + " [" + property.get(PROPERTY_VALUE) + "]")
                    .collect(Collectors.joining(", "));
        } catch (JsonProcessingException e) {
            LOGGER.error("Error reading JSON: {}", e.getOriginalMessage());
        }
        return null;
    }

    public Optional<ErrorDetail> getErrorDetailsByCode(String errorCode) throws JsonProcessingException {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("code", errorCode);
        List<String> errorKeys = Arrays.stream(settings.getFileErrorPostfixKey())
                .map(value -> errorCode + "." + value)
                .collect(Collectors.toList());
        errorDetails.putAll(getPropertiesByPartialKey(errorKeys, settings.getFileUrl()).stream()
                .flatMap(property -> {
                            String propertyKey = property.get(PROPERTY_KEY);
                            return Collections.singletonMap(
                                    propertyKey.substring(propertyKey.lastIndexOf(".") + 1).toLowerCase(),
                                    property.get(PROPERTY_VALUE)
                            ).entrySet().stream();
                        }
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return Optional.ofNullable(objectMapper.convertValue(errorDetails, ErrorDetail.class))
                .flatMap(details -> {
                    if (StringUtils.isEmpty(details.getName()) && StringUtils.isEmpty(details.getDescription())) {
                        return Optional.empty();
                    }
                    return Optional.of(details);
                });
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
