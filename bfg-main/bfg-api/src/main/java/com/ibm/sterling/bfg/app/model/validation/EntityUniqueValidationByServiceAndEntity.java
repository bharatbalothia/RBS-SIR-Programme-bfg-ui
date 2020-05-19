package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EntityUniqueValidationByServiceAndEntity implements ConstraintValidator<EntityValid, Entity> {

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity,
                           ConstraintValidatorContext constraintValidatorContext) {
        return !entityService.existsByServiceAndEntity(entity.getService(),
                entity.getEntity());
    }

}
