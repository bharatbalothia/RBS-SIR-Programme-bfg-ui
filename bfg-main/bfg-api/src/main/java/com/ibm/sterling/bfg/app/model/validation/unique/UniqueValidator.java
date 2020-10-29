package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {
    private static final Logger LOG = LogManager.getLogger(UniqueValidator.class);

    @Autowired
    private ApplicationContext applicationContext;
    private FieldValueExists service;
    private String fieldName;

    @Override
    public void initialize(Unique unique) {
        fieldName = unique.fieldName();
        Class<? extends FieldValueExists> serviceClass = unique.service();
        service = applicationContext.getBean(serviceClass);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Validation field {} of service {}", fieldName, service);
        Boolean isUniqueField = Optional.ofNullable(service)
                .map(validService -> !service.fieldValueExists(value, fieldName))
                .orElse(false);
        LOG.info("Is {} unique for {}: {}", value, fieldName, isUniqueField);
        return isUniqueField;
    }

}
