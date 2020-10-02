package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Formatter;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class RouteValidator implements ConstraintValidator<RouteValid, Entity> {
    private static final Logger LOG = LogManager.getLogger(RouteValidator.class);
    private static final String ROUTE_INBOUND = "routeInbound";
    private static final String INBOUND_DIR = "inboundDir";
    private static final String INBOUND_ROUTING_RULE = "inboundRoutingRule";

    @Override
    public boolean isValid(Entity entity,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Route validation");
        BiFunction<String, Boolean, String> emptyFieldMessage =
                (fieldName, inboundRoute) ->
                        new Formatter().format("%s cannot be empty because the " + ROUTE_INBOUND + " has the value %s",
                                fieldName, inboundRoute).toString();

        Boolean isRouteInbound = entity.getRouteInbound();
        boolean isRouteInboundNotNull = isValidInboundRoute(ROUTE_INBOUND, isRouteInbound,
                ROUTE_INBOUND + " has to be present",
                constraintValidatorContext, StringUtils::isEmpty);

        if (isRouteInboundNotNull) {
            if (isRouteInbound) {
                return isValidInboundRoute(RouteFieldName.ROUTE_REQUESTORDN.fieldName(), entity.getInboundRequestorDN(),
                        emptyFieldMessage.apply(RouteFieldName.ROUTE_REQUESTORDN.name(), entity.getRouteInbound()),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidInboundRoute(RouteFieldName.ROUTE_RESPONDERDN.fieldName(), entity.getInboundResponderDN(),
                        emptyFieldMessage.apply(RouteFieldName.ROUTE_RESPONDERDN.name(), entity.getRouteInbound()),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidInboundRoute(RouteFieldName.ROUTE_SERVICE.fieldName(), entity.getInboundService(),
                        emptyFieldMessage.apply(RouteFieldName.ROUTE_SERVICE.name(), entity.getRouteInbound()),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidInboundRoute(RouteFieldName.ROUTE_REQUESTTYPE.fieldName(), entity.getInboundRequestType(),
                        emptyFieldMessage.apply(RouteFieldName.ROUTE_REQUESTTYPE.name(), entity.getRouteInbound()),
                        constraintValidatorContext, CollectionUtils::isEmpty)

                        & isValidInboundRoute(INBOUND_DIR, entity.getInboundDir(),
                        emptyFieldMessage.apply(INBOUND_DIR, entity.getRouteInbound()),
                        constraintValidatorContext, (Boolean inboundDir) -> !Optional.ofNullable(inboundDir).isPresent())

                        & isValidInboundRoute(INBOUND_ROUTING_RULE, entity.getInboundRoutingRule(),
                        emptyFieldMessage.apply(INBOUND_ROUTING_RULE, entity.getRouteInbound()),
                        constraintValidatorContext, (Boolean inboundRoutingRule) -> !Optional.ofNullable(inboundRoutingRule).isPresent());
            }
            return true;
        }
        return false;
    }

    private <T> boolean isValidInboundRoute(String fieldName, T fieldValue, String message,
                                            ConstraintValidatorContext constraintValidatorContext, Predicate<T> validation) {
        if (validation.test(fieldValue)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
