package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EntityUniqueValidatorPut implements ConstraintValidator<EntityUniquePut, Entity> {

    private static final Logger LOG = LogManager.getLogger(EntityUniqueValidatorPut.class);

    @Autowired
    private EntityService entityService;
    private String fieldName;

    @Override
    public void initialize(EntityUniquePut unique) {
        fieldName = unique.fieldName();
    }

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext context) {
        LOG.info("Validation field {} of service {}", fieldName, entityService);
        Boolean isUniqueField = Optional.ofNullable(entityService)
                .map(validService -> !validService.fieldValueExistsPut(entity, fieldName))
                .orElse(false);
        LOG.info("Is {} unique for {}: {}", entity, fieldName, isUniqueField);
        return isUniqueField;
    }
}
