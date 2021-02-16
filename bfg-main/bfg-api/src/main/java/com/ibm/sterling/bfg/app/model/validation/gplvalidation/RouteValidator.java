package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.validation.Field;
import com.ibm.sterling.bfg.app.model.validation.GenericValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public class RouteValidator extends GenericValidator {

    private static final Logger LOG = LogManager.getLogger(RouteValidator.class);
    private static final String EMPTY_STRING_FIELD_FORMAT = "%s cannot be blank because the Route Inbound has the value %s";
    private static final String EMPTY_LIST_FIELD_FORMAT = "%s cannot be empty because the Route Inbound has the value %s";
    private static final String DN_REGEXP = "^(?:(?:(?:(?:cn|ou)=[^,]+,?)+),[\\s]*)*(?:o=[a-z]{6}[0-9a-z]{2}){1},[\\s]*o=swift$";

    public boolean validateRouteFields(Entity entity, ConstraintValidatorContext constraintValidatorContext,
                                       Function<Entity, Optional<Entity>> getEntityWithAttributesOfRoutingRules) {
        LOG.info("Route validation");
        Boolean isRouteInbound = entity.getRouteInbound();
        boolean isRouteInboundNotNull = isValidField(
                RouteField.ROUTE_INBOUND,
                isRouteInbound,
                constraintValidatorContext,
                StringUtils::isEmpty,
                fieldName -> fieldName + " has to be present"
        );
        if (isRouteInboundNotNull) {
            if (isRouteInbound) {
                Optional<Entity> entityWithAttributesOfRoutingRules = getEntityWithAttributesOfRoutingRules.apply(entity);
                return isValidFieldGroup(
                        new HashMap<Field, String>() {
                            {
                                put(RouteField.ROUTE_REQUESTORDN, entity.getInboundRequestorDN());
                                put(RouteField.ROUTE_RESPONDERDN, entity.getInboundResponderDN());
                                put(RouteField.ROUTE_SERVICE, entity.getInboundService());
                            }
                        },
                        constraintValidatorContext,
                        StringUtils::isEmpty,
                        emptyFieldTemplateOnSwitchValue(EMPTY_STRING_FIELD_FORMAT, String.valueOf(entity.getRouteInbound()))
                )
                        & isValidField(
                        RouteField.ROUTE_REQUESTTYPE,
                        entity.getInboundRequestType(),
                        constraintValidatorContext,
                        CollectionUtils::isEmpty,
                        emptyFieldTemplateOnSwitchValue(EMPTY_LIST_FIELD_FORMAT, String.valueOf(entity.getRouteInbound()))
                )
                        & isValidFieldGroup(
                        new HashMap<Field, String>() {
                            {
                                put(RouteField.ROUTE_REQUESTORDN, Optional.ofNullable(entity.getInboundRequestorDN()).orElse(""));
                                put(RouteField.ROUTE_RESPONDERDN, Optional.ofNullable(entity.getInboundResponderDN()).orElse(""));
                            }
                        },
                        constraintValidatorContext,
                        fieldValue -> !Pattern.compile(DN_REGEXP).matcher(fieldValue).matches(),
                        fieldName -> "Please match the requested format for " + fieldName
                ) & validate(
                        "routingRules",
                        entity,
                        constraintValidatorContext,
                        validatedEntity -> entityWithAttributesOfRoutingRules.isPresent(),
                        "Entity properties should be unique for requester DN, responder DN, service, and request types. " +
                                "These match the entity " + entityWithAttributesOfRoutingRules.map(Entity::getEntity).orElse("") +
                                ". Please correct the properties and try again, or cancel"
                );
            }
            return true;
        }
        return false;
    }

}
