package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EntityUniqueValidator implements ConstraintValidator<EntityUnique, Object> {

    private static final Logger LOG = LogManager.getLogger(EntityUniqueValidator.class);

    @Autowired
    private EntityService entityService;
    private String fieldName;

    @Override
    public void initialize(EntityUnique unique) {
        fieldName = unique.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Validation field {} of service {}", fieldName, entityService);
        Boolean isUniqueField = Optional.ofNullable(entityService)
                .map(validService -> !validService.fieldValueExists(value, fieldName))
                .orElse(false);
        LOG.info("Is {} unique for {}: {}", value, fieldName, isUniqueField);
        return isUniqueField;
    }

}
