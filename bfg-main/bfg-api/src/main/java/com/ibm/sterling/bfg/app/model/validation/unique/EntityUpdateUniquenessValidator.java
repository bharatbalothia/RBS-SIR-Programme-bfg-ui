package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityUpdateUniquenessValidator implements ConstraintValidator<EntityUpdateUniqueness, Entity> {

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext constraintValidatorContext) {
        Function<String, String> fieldMessage = (fieldName) ->
                new Formatter().format("%s has to be unique", fieldName).toString();
        Integer entityId = entity.getEntityId();
        Map<String, String> entityServiceMap = new HashMap<String, String>() {
            {
                put("entity", entity.getEntity());
                put("service", entity.getService());
            }
        };
        return isUniqueConstraint(entityId, entity.getMqQueueOut(), EntityFieldName.MQQUEUEOUT.fieldName(), EntityFieldName.MQQUEUEOUT.name(),
                fieldMessage.apply(EntityFieldName.MQQUEUEOUT.name()), constraintValidatorContext)
                & isUniqueConstraint(entityId, entity.getMailboxPathOut(), EntityFieldName.MAILBOXPATHOUT.fieldName(), EntityFieldName.MAILBOXPATHOUT.name(),
                fieldMessage.apply(EntityFieldName.MAILBOXPATHOUT.name()), constraintValidatorContext)
                & isUniqueConstraint(entityId, entityServiceMap, EntityFieldName.ENTITY_SERVICE.fieldName(), EntityFieldName.ENTITY_SERVICE.name(),
                fieldMessage.apply("ENTITY or SERVICE"), constraintValidatorContext);
    }

    private <T> boolean isUniqueConstraint(Integer entityId, T fieldValue, String fieldName, String dbName,
                                           String message, ConstraintValidatorContext constraintValidatorContext) {
        if (entityService.fieldValueExistsBesidesItself(entityId, fieldValue, dbName)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
