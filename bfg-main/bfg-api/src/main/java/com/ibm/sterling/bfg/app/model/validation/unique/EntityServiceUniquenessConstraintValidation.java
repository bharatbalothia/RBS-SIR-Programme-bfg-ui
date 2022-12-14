package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EntityServiceUniquenessConstraintValidation implements ConstraintValidator<EntityServiceUniquenessConstraint, Entity> {

    private static final Logger LOG = LogManager.getLogger(EntityServiceUniquenessConstraintValidation.class);

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Validation by service and entity of entityService {}", entityService);
        Boolean isUniqueFields = Optional.ofNullable(entityService)
                .map(validService -> !validService.existsByServiceAndEntity(entity.getService(),
                        entity.getEntity()))
                .orElse(false);
        LOG.info("Are service and entity unique {}", isUniqueFields);
        return isUniqueFields;
    }

}
