package com.ibm.sterling.bfg.app.service.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.entity.EntityApprovalException;
import com.ibm.sterling.bfg.app.exception.entity.SWIFTNetRoutingRuleException;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleBfgUiRestResponse;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleRequest;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleServiceResponse;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.ibm.sterling.bfg.app.model.changecontrol.Operation.*;

@Service
public class SWIFTNetRoutingRuleService {
    private static final Logger LOG = LogManager.getLogger(SWIFTNetRoutingRuleService.class);

    private static final String CREATION_APPROVAL_ERROR = "The Entity is not approved. Error creating routing rules";
    private static final String DELETING_APPROVAL_ERROR = "The Entity is not approved. Error deleting routing rules";
    private static final String RULE_EXISTS_ERROR = "A route already exists with this name";
    private static final String NO_RULES_FOUND_WARNING = "No previously created rules were found";
    private static final String GET_RULES_ERROR = "Failure to get RR from SBI";

    @Value("${routingRule.url}")
    private String routingRuleViewUrl;

    @Value("${routingRule.create.url}")
    private String routingRuleCreationUrl;

    @Value("${routingRule.delete.url}")
    private String routingRuleDeleteUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private EntityService entityService;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

    public SWIFTNetRoutingRuleServiceResponse executeRoutingRuleOperation(Operation operation, Entity entity, String changer)
            throws JsonProcessingException {
        LOG.info("Trying to {} RR for Entity {}", operation, entity.getEntity());
        List<String> routingRulesByEntityName = Optional.ofNullable(getRoutingRulesByEntityName(entity.getEntity())).orElseGet(ArrayList::new);
        if (entity.getRouteInbound()) {
            if (CREATE.equals(operation)) {
                if (routingRulesByEntityName.isEmpty())
                    return createRoutingRules(new SWIFTNetRoutingRuleRequest(entity, changer));
                throw new EntityApprovalException(routingRulesByEntityName.stream()
                        .map(ruleName -> Collections.singletonMap(ruleName, RULE_EXISTS_ERROR))
                        .collect(Collectors.toList()), CREATION_APPROVAL_ERROR);
            } else if (DELETE.equals(operation)) {
                if (routingRulesByEntityName.isEmpty())
                    return new SWIFTNetRoutingRuleServiceResponse(
                            null, Collections.singletonList(Collections.singletonMap(operation.name(), NO_RULES_FOUND_WARNING)));
                deleteRoutingRules(routingRulesByEntityName);
            }
        }
        if (UPDATE.equals(operation)) {
            List<Map<String, String>> warnings = null;
            if (routingRulesByEntityName.isEmpty()) {
                Boolean routeInboundOfOldEntity = entityService.findById(entity.getEntityId())
                        .map(record -> !ObjectUtils.isEmpty(record.getInboundRequestorDN()) &&
                                !ObjectUtils.isEmpty(record.getInboundResponderDN()))
                        .orElse(false);
                if (routeInboundOfOldEntity) {
                    warnings = Collections.singletonList(Collections.singletonMap(operation.name(), NO_RULES_FOUND_WARNING));
                }
            } else deleteRoutingRules(routingRulesByEntityName);
            if (entity.getRouteInbound()) {
                SWIFTNetRoutingRuleServiceResponse response = createRoutingRules(new SWIFTNetRoutingRuleRequest(entity, changer));
                response.setWarnings(warnings);
                return response;
            }
        }
        return new SWIFTNetRoutingRuleServiceResponse();
    }

    private SWIFTNetRoutingRuleServiceResponse createRoutingRules(SWIFTNetRoutingRuleRequest swiftNetRoutingRuleRequest)
            throws JsonProcessingException {
        String routingRuleResponse;
        try {
            routingRuleResponse = new RestTemplate().postForObject(
                    routingRuleCreationUrl,
                    new HttpEntity<>(swiftNetRoutingRuleRequest, apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            LOG.error("Failure on creating the RR in SBI");
            throw new SWIFTNetRoutingRuleException(apiDetailsHandler.processErrorMessage(e), e.getStatusCode(), CREATION_APPROVAL_ERROR);
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(routingRuleResponse));
        List<SWIFTNetRoutingRuleBfgUiRestResponse> swiftNetRoutingRuleBfgUiRestResponse =
                objectMapper.convertValue(root, new TypeReference<List<SWIFTNetRoutingRuleBfgUiRestResponse>>() {
                });
        List<Object> errorList = swiftNetRoutingRuleBfgUiRestResponse.stream()
                .filter(ruleResponse -> !HttpStatus.valueOf(ruleResponse.getCode()).is2xxSuccessful())
                .map(ruleResponse -> (Object) Collections.singletonMap(ruleResponse.getRoutingRuleName(), ruleResponse.getFailCause()))
                .collect(Collectors.toList());
        if (errorList.size() > 0) {
            if (errorList.size() < swiftNetRoutingRuleBfgUiRestResponse.size()) {
                Entity entity = new Entity();
                entity.setEntity(swiftNetRoutingRuleRequest.getEntityName());
                executeRoutingRuleOperation(DELETE, entity, null);
            }
            throw new EntityApprovalException(errorList, CREATION_APPROVAL_ERROR);
        }
        return new SWIFTNetRoutingRuleServiceResponse(swiftNetRoutingRuleBfgUiRestResponse);
    }

    private List<String> getRoutingRulesByEntityName(String entityName) throws JsonProcessingException {
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(
                    routingRuleViewUrl + "?entity-name=" + entityName,
                    HttpMethod.GET,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
            JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
            return objectMapper.convertValue(root, new TypeReference<List<String>>() {
            });
        } catch (HttpStatusCodeException e) {
            LOG.error("Failure on getting the RR for Entity {} from SBI", entityName);
            throw new SWIFTNetRoutingRuleException(apiDetailsHandler.processErrorMessage(e), e.getStatusCode(),
                    GET_RULES_ERROR);
        }
    }

    private void deleteRoutingRules(List<String> routingRulesByEntityName) {
        HttpEntity<String> httpEntity = new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password));
        routingRulesByEntityName.forEach(routingRule -> {
            try {
                new RestTemplate().exchange(
                        routingRuleDeleteUrl + routingRule,
                        HttpMethod.DELETE,
                        httpEntity,
                        String.class
                );
            } catch (HttpStatusCodeException e) {
                LOG.error("Failure on deleting the RR in SBI");
                throw new SWIFTNetRoutingRuleException(apiDetailsHandler.processErrorMessage(e), e.getStatusCode(),
                        DELETING_APPROVAL_ERROR);
            }
        });
    }

}
