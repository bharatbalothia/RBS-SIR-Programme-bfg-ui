package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.EntityApprovalException;
import com.ibm.sterling.bfg.app.exception.SWIFTNetRoutingRuleException;
import com.ibm.sterling.bfg.app.model.changeControl.Operation;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleRequest;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleBfguiRestResponse;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.model.changeControl.Operation.CREATE;
import static com.ibm.sterling.bfg.app.model.changeControl.Operation.DELETE;
import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class SWIFTNetRoutingRuleService {

    @Value("${routingRule.url}")
    private String routingRuleViewUrl;

    @Value("${routingRule.create.url}")
    private String routingRuleCreationUrl;

    @Value("${routingRule.delete.url}")
    private String routingRuleDeleteUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    public SWIFTNetRoutingRuleServiceResponse executeRoutingRuleOperation(Operation operation, Entity entity, String changer) throws JsonProcessingException {
        List<String> routingRulesByEntityName = getRoutingRulesByEntityName(entity.getEntity());
        if (operation.equals(CREATE)) {
            if (routingRulesByEntityName.isEmpty())
                return createRoutingRules(new SWIFTNetRoutingRuleRequest(entity, changer));
            throw new EntityApprovalException(routingRulesByEntityName.stream()
                    .map(ruleName -> Collections.singletonMap(ruleName, "A route already exists with this name."))
                    .collect(Collectors.toList()), "Error creating routing rules in Entity");
        } else if (operation.equals(DELETE)) {
            if (!routingRulesByEntityName.isEmpty())
                deleteRoutingRules(routingRulesByEntityName);
        }
        return new SWIFTNetRoutingRuleServiceResponse();
    }

    private SWIFTNetRoutingRuleServiceResponse createRoutingRules(SWIFTNetRoutingRuleRequest swiftNetRoutingRuleRequest)
            throws JsonProcessingException {
        String routingRuleResponse = null;
        try {
            routingRuleResponse = new RestTemplate().postForObject(
                    routingRuleCreationUrl,
                    new HttpEntity<>(swiftNetRoutingRuleRequest, getHttpHeaders()),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            processErrorMessage(e, "Error creating routing rules in Entity");
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(routingRuleResponse));
        List<SWIFTNetRoutingRuleBfguiRestResponse> swiftNetRoutingRuleBfguiRestResponse =
                objectMapper.convertValue(root, new TypeReference<List<SWIFTNetRoutingRuleBfguiRestResponse>>() {
                });
        Map<String, String> errorMap = swiftNetRoutingRuleBfguiRestResponse.stream()
                .filter(ruleResponse -> !HttpStatus.valueOf(ruleResponse.getCode()).is2xxSuccessful())
                .collect(Collectors.toMap(
                        SWIFTNetRoutingRuleBfguiRestResponse::getRoutingRuleName, SWIFTNetRoutingRuleBfguiRestResponse::getFailCause
                ));
        if (errorMap.size() > 0) {
            if (errorMap.size() < swiftNetRoutingRuleBfguiRestResponse.size()) {
                Entity entity = new Entity();
                entity.setEntity(swiftNetRoutingRuleRequest.getEntityName());
                executeRoutingRuleOperation(DELETE, entity, null);
            }
            throw new EntityApprovalException(Collections.singletonList(errorMap), "Error creating routing rules in Entity");
        }
        return new SWIFTNetRoutingRuleServiceResponse(swiftNetRoutingRuleBfguiRestResponse, null);
    }

    private List<String> getRoutingRulesByEntityName(String entityName) throws JsonProcessingException {
        ResponseEntity<String> response = new RestTemplate().exchange(
                routingRuleViewUrl + "?entity-name=" + entityName,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return objectMapper.convertValue(root, new TypeReference<List<String>>() {
        });
    }

    private void deleteRoutingRules(List<String> routingRulesByEntityName) {
        routingRulesByEntityName.forEach(routingRule -> {
            try {
                new RestTemplate().delete(routingRuleDeleteUrl + routingRule);
            } catch (HttpStatusCodeException e) {
                processErrorMessage(e, "Error deleting routing rules in Entity");
            }
        });
    }

    private void processErrorMessage(HttpStatusCodeException e, String codeMessage) {
        Optional.ofNullable(e.getMessage()).ifPresent(errorMessage -> {
            List<Map<String, Object>> errorList = null;
            Matcher matcher = Pattern.compile("\\{.*}").matcher(errorMessage);
            if (matcher.find()) {
                String errorMessageList = "[" + matcher.group(0) + "]";
                try {
                    errorList = new ObjectMapper().readValue(errorMessageList, new TypeReference<List<Map<String, Object>>>() {
                    });
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }
            Map<String, List<Object>> errorMap = Optional.ofNullable(errorList).map(errors -> {
                Map<String, List<Object>> routingRuleErrors = new HashMap<>();
                errors.forEach(error -> {
                    String key = String.valueOf(error.get("attribute"));
                    if (routingRuleErrors.containsKey(key))
                        routingRuleErrors.get(key).add(error.get("message"));
                    else
                        routingRuleErrors.put(key, new ArrayList<>(Collections.singletonList(error.get("message"))));
                });
                return routingRuleErrors;
            }).orElseGet(() -> Collections.singletonMap("error", Collections.singletonList(errorMessage)));
            throw new SWIFTNetRoutingRuleException(errorMap, e.getStatusCode(), codeMessage);
        });
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

}
