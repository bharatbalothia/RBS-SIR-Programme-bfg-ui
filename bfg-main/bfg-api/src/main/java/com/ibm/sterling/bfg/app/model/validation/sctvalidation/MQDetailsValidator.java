package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Formatter;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class MQDetailsValidator implements ConstraintValidator<MQValid, Entity> {

    private static final Logger LOG = LogManager.getLogger(MQDetailsValidator.class);

    private static final String DIRECT_PARTICIPANT_TYPE = "DIRECT";
    private static final String INDIRECT_PARTICIPANT_TYPE = "INDIRECT";

    @Override
    public boolean isValid(Entity entity,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("MQ Details validation");
        boolean isPositiveFields = isValidMQDetails(MQDetailsFieldName.MQ_PORT.fieldName(), entity.getMqPort(),
                "The " + MQDetailsFieldName.MQ_PORT.name() + " should be positive",
                constraintValidatorContext, (Integer mqPort) -> Optional.ofNullable(mqPort)
                        .map(port -> port <= 0).orElse(false)
        )
                & isValidMQDetails(MQDetailsFieldName.MQ_SESSIONTIMEOUT.fieldName(), entity.getMqSessionTimeout(),
                "The " + MQDetailsFieldName.MQ_SESSIONTIMEOUT.name() + " should be positive",
                constraintValidatorContext, (Integer mqSessionTimeout) -> Optional.ofNullable(mqSessionTimeout)
                        .map(sessionTimeout -> sessionTimeout <= 0).orElse(false)
        );

        BiFunction<String, String, String> emptyFieldMessage =
                (fieldName, directParticipant) ->
                        new Formatter().format("The %s cannot be empty because the Entity Participant " +
                                        "Type has the value %s",
                                fieldName, directParticipant).toString();

        String entityParticipantType = entity.getEntityParticipantType();
        boolean isEntityParticipantTypeNotNull = isValidMQDetails(MQDetailsFieldName.ENTITY_PARTICIPANT_TYPE.fieldName(),
                entityParticipantType,
                "The " + MQDetailsFieldName.ENTITY_PARTICIPANT_TYPE.name() + " cannot be empty",
                constraintValidatorContext, StringUtils::isEmpty);

        if (isEntityParticipantTypeNotNull) {
            if (entity.getEntityParticipantType().equals(INDIRECT_PARTICIPANT_TYPE)) {
                return isPositiveFields & isValidMQDetails(MQDetailsFieldName.DIRECT_PARTICIPANT.fieldName(),
                        entity.getDirectParticipant(),
                        emptyFieldMessage.apply(MQDetailsFieldName.DIRECT_PARTICIPANT.name(), INDIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty);
            } else if (entity.getEntityParticipantType().equals(DIRECT_PARTICIPANT_TYPE)) {
                return isPositiveFields
                        & isValidMQDetails(MQDetailsFieldName.MQ_HOST.fieldName(), entity.getMqHost(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_HOST.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_PORT.fieldName(), entity.getMqPort(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_PORT.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, (Integer port) -> !Optional.ofNullable(port).isPresent())

                        & isValidMQDetails(MQDetailsFieldName.MQ_QMANAGER.fieldName(), entity.getMqQManager(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_QMANAGER.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_CHANNEL.fieldName(), entity.getMqChannel(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_CHANNEL.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_QNAME.fieldName(), entity.getMqQueueName(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_QNAME.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_QBINDING.fieldName(), entity.getMqQueueBinding(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_QBINDING.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_QCONTEXT.fieldName(), entity.getMqQueueContext(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_QCONTEXT.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_DEBUG.fieldName(), entity.getMqDebug(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_DEBUG.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_SSLOPTION.fieldName(), entity.getMqSSLOptions(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_SSLOPTION.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_SSLCIPHERS.fieldName(), entity.getMqSSLCiphers(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_SSLCIPHERS.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_SSLSYSTEMCERTID.fieldName(), entity.getMqSSLKeyCert(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_SSLSYSTEMCERTID.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_SSLCACERTID.fieldName(), entity.getMqSSLCaCert(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_SSLCACERTID.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_HEADER.fieldName(), entity.getMqHeader(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_HEADER.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidMQDetails(MQDetailsFieldName.MQ_SESSIONTIMEOUT.fieldName(), entity.getMqSessionTimeout(),
                        emptyFieldMessage.apply(MQDetailsFieldName.MQ_SESSIONTIMEOUT.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, (Integer sessionTimeout) ->
                                !Optional.ofNullable(sessionTimeout).isPresent());
            }
            return isPositiveFields;
        }
        return false;
    }

    private <T> boolean isValidMQDetails(String fieldName, T fieldValue, String message,
                                         ConstraintValidatorContext constraintValidatorContext, Predicate<T> validation) {
        if (validation.test(fieldValue)) {
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
