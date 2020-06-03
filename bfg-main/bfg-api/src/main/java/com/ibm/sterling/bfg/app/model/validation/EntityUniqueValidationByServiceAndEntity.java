package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EntityUniqueValidationByServiceAndEntity implements ConstraintValidator<EntityValid, Entity> {

    private static final Logger LOG = LogManager.getLogger(EntityUniqueValidationByServiceAndEntity.class);

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Validation by service and entity of entityService {}", entityService);
        Boolean isUniqueFields = Optional.ofNullable(entityService)
                .map(validService -> !validService.existsByServiceAndEntityPut(entity.getService(),
                        entity.getEntity()))
                .orElse(false);
        LOG.info("Are service and entity unique {}", isUniqueFields);
        return isUniqueFields;
    }

}
