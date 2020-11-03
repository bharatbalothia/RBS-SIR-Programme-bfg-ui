package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class RouteUpdateValidator extends RouteValidator implements ConstraintValidator<RouteUpdateValid, Entity> {

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext constraintValidatorContext) {
        return validateRouteFields(
                entity,
                constraintValidatorContext,
                entityWithAttributesOfRoutingRules -> Optional.ofNullable(
                        entityService.getEntityWithAttributesOfRoutingRulesBesidesItself(
                                entityWithAttributesOfRoutingRules.getInboundRequestorDN(),
                                entityWithAttributesOfRoutingRules.getInboundResponderDN(),
                                entityWithAttributesOfRoutingRules.getInboundService(),
                                entityWithAttributesOfRoutingRules.getInboundRequestType(),
                                entityWithAttributesOfRoutingRules.getEntityId()
                        )
                )
        );
    }

}
