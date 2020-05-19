package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EntityUniqueValidator implements ConstraintValidator<EntityUnique, Object> {

    @Autowired
    private EntityService entityService;
    private String fieldName;

    @Override
    public void initialize(EntityUnique unique) {
        fieldName = unique.fieldName();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        return !entityService.fieldValueExists(object, fieldName);
    }

}
