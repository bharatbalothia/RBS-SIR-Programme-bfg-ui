package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.validation.Field;
import com.ibm.sterling.bfg.app.model.validation.GenericValidator;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Optional;

public class DetailsValidator extends GenericValidator implements ConstraintValidator<DetailsValid, Entity> {

    private static final Logger LOG = LogManager.getLogger(DetailsValidator.class);
    private static final String DIRECT_PARTICIPANT_TYPE = "DIRECT";
    private static final String INDIRECT_PARTICIPANT_TYPE = "INDIRECT";
    private static final String EMPTY_STRING_FIELD_FORMAT = "%s cannot be blank because the Entity Participant Type has the value %s";
    private static final String EMPTY_NUMERIC_FIELD_FORMAT = "%s cannot be null because the Entity Participant Type has the value %s";

    @Autowired
    private EntityService entityService;

    @Override
    public boolean isValid(Entity entity, ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("MQ Details validation");
        boolean isPositiveFields = isValidFieldGroup(
                new HashMap<Field, Integer>() {
                    {
                        put(DetailsField.MQ_PORT, entity.getMqPort());
                        put(DetailsField.MQ_SESSIONTIMEOUT, entity.getMqSessionTimeout());
                    }
                },
                constraintValidatorContext,
                fieldValue -> Optional.ofNullable(fieldValue).map(value -> value < 0).orElse(false),
                fieldName -> fieldName + " should be a positive number or 0"
        );
        String entityParticipantType = entity.getEntityParticipantType();
        boolean isEntityParticipantTypeNotNull = isValidField(
                DetailsField.ENTITY_PARTICIPANT_TYPE,
                entityParticipantType,
                constraintValidatorContext,
                ObjectUtils::isEmpty,
                fieldName -> fieldName + " cannot be blank"
        );
        if (isEntityParticipantTypeNotNull) {
            boolean isValidFields = true;
            if (entityParticipantType.equals(INDIRECT_PARTICIPANT_TYPE)) {
                isValidFields = isValidField(
                        DetailsField.DIRECT_PARTICIPANT,
                        entity.getDirectParticipant(),
                        constraintValidatorContext,
                        ObjectUtils::isEmpty,
                        emptyFieldTemplateOnSwitchValue(EMPTY_STRING_FIELD_FORMAT, INDIRECT_PARTICIPANT_TYPE)
                )
                        & isValidField(
                        DetailsField.DIRECT_PARTICIPANT,
                        entity.getDirectParticipant(),
                        constraintValidatorContext,
                        fieldValue -> !entityService.findEntityNameForParticipants(entity.getEntityId())
                                .contains(entity.getDirectParticipant()),
                        fieldName -> "The entity doesn't have a valid " + fieldName
                );
            } else if (entityParticipantType.equals(DIRECT_PARTICIPANT_TYPE)) {
                isValidFields = isValidFieldGroup(
                        new HashMap<Field, String>() {
                            {
                                put(DetailsField.MQ_HOST, entity.getMqHost());
                                put(DetailsField.MQ_QMANAGER, entity.getMqQManager());
                                put(DetailsField.MQ_CHANNEL, entity.getMqChannel());
                                put(DetailsField.MQ_QNAME, entity.getMqQueueName());
                                put(DetailsField.MQ_QBINDING, entity.getMqQueueBinding());
                                put(DetailsField.MQ_QCONTEXT, entity.getMqQueueContext());
                                put(DetailsField.MQ_DEBUG, entity.getMqDebug());
                                put(DetailsField.MQ_SSLOPTION, entity.getMqSSLOptions());
                                put(DetailsField.MQ_SSLCIPHERS, entity.getMqSSLCiphers());
                                put(DetailsField.MQ_SSLSYSTEMCERTID, entity.getMqSSLKeyCert());
                                put(DetailsField.MQ_SSLCACERTID, entity.getMqSSLCaCert());
                                put(DetailsField.MQ_HEADER, entity.getMqHeader());
                                put(DetailsField.REQUESTORDN, entity.getRequestorDN());
                                put(DetailsField.RESPONDERDN, entity.getResponderDN());
                                put(DetailsField.REQUESTTYPE, entity.getRequestType());
                                put(DetailsField.SERVICENAME, entity.getServiceName());
                            }
                        },
                        constraintValidatorContext,
                        ObjectUtils::isEmpty,
                        emptyFieldTemplateOnSwitchValue(EMPTY_STRING_FIELD_FORMAT, DIRECT_PARTICIPANT_TYPE)
                ) & isValidFieldGroup(
                        new HashMap<Field, Integer>() {
                            {
                                put(DetailsField.MQ_PORT, entity.getMqPort());
                                put(DetailsField.MQ_SESSIONTIMEOUT, entity.getMqSessionTimeout());
                            }
                        },
                        constraintValidatorContext,
                        fieldValue -> !Optional.ofNullable(fieldValue).isPresent(),
                        emptyFieldTemplateOnSwitchValue(EMPTY_NUMERIC_FIELD_FORMAT, DIRECT_PARTICIPANT_TYPE)
                );
            }
            return isPositiveFields & isValidFields;
        }
        return false;
    }

}
