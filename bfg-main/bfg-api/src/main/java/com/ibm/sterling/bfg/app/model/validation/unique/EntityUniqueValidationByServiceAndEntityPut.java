package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EntityUniqueValidationByServiceAndEntityPut implements ConstraintValidator<EntityValidPut, Entity> {

    private static final Logger LOG = LogManager.getLogger(EntityUniqueValidationByServiceAndEntityPut.class);

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext context) {
        LOG.info("Validation by entity and service of entityService {}", entityService);
        Boolean isUniqueFields = Optional.ofNullable(entityService)
                .map(validService -> !validService.existsByServiceAndEntityPut(entity))
                .orElse(false);
        LOG.info("Are entity and service unique {}", isUniqueFields);
        return isUniqueFields;
    }
}
