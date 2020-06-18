package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class MqQueueUniquePutValidator implements ConstraintValidator<MqQueueUniquePut, Entity> {

    private static final Logger LOG = LogManager.getLogger(MqQueueUniquePutValidator.class);

    @Autowired
    private EntityService entityService;
    private String fieldName;

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext context) {
        String mqQueueOut = entity.getMqQueueOut();
        if (mqQueueOut == null) {
            return false;
        }
        LOG.info("Validation MqQueueOut {} for edited entity {}", mqQueueOut, entity);
        Boolean isUniqueField = Optional.ofNullable(entityService)
                .map(validService -> !validService.fieldValueExistsPut(entity))
                .orElse(false);
        LOG.info("Is {} unique for {}: {}", mqQueueOut, entity, isUniqueField);
        return isUniqueField;
    }
}
