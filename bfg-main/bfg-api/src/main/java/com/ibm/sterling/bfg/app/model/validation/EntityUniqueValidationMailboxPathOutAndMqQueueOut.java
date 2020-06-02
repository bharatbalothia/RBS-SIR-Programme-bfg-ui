package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EntityUniqueValidationMailboxPathOutAndMqQueueOut implements ConstraintValidator<MailboxMqQueueOutValid, Entity> {

    private static final Logger LOG = LogManager.getLogger(EntityUniqueValidationMailboxPathOutAndMqQueueOut.class);

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext context) {
        LOG.info("Validation by mailboxPathOut and mqQueueOut of entityService {}", entityService);
        Boolean isUniqueFields = Optional.ofNullable(entityService)
                .map(validService -> !validService.existsByMqQueueOutAndMailboxPathOut(entity))
                .orElse(false);
        LOG.info("Are mailboxPathOut and mqQueueOut unique {}", isUniqueFields);
        return isUniqueFields;
    }
}
