package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Formatter;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class DetailsValidator implements ConstraintValidator<DetailsValid, Entity> {

    private static final Logger LOG = LogManager.getLogger(DetailsValidator.class);

    private static final String DIRECT_PARTICIPANT_TYPE = "DIRECT";
    private static final String INDIRECT_PARTICIPANT_TYPE = "INDIRECT";

    @Override
    public boolean isValid(Entity entity,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("MQ Details validation");
        boolean isPositiveFields = isValidDetail(DetailsFieldName.MQ_PORT.fieldName(), entity.getMqPort(),
                DetailsFieldName.MQ_PORT.name() + " should be positive",
                constraintValidatorContext, (Integer mqPort) -> Optional.ofNullable(mqPort)
                        .map(port -> port <= 0).orElse(false)
        )
                & isValidDetail(DetailsFieldName.MQ_SESSIONTIMEOUT.fieldName(), entity.getMqSessionTimeout(),
                DetailsFieldName.MQ_SESSIONTIMEOUT.name() + " should be positive",
                constraintValidatorContext, (Integer mqSessionTimeout) -> Optional.ofNullable(mqSessionTimeout)
                        .map(sessionTimeout -> sessionTimeout <= 0).orElse(false)
        );

        BiFunction<String, String, String> emptyFieldMessage =
                (fieldName, participantType) ->
                        new Formatter().format("%s cannot be empty because the Entity Participant " +
                                        "Type has the value %s",
                                fieldName, participantType).toString();

        String entityParticipantType = entity.getEntityParticipantType();
        boolean isEntityParticipantTypeNotNull = isValidDetail(DetailsFieldName.ENTITY_PARTICIPANT_TYPE.fieldName(),
                entityParticipantType,
                DetailsFieldName.ENTITY_PARTICIPANT_TYPE.name() + " cannot be empty",
                constraintValidatorContext, StringUtils::isEmpty);

        if (isEntityParticipantTypeNotNull) {
            if (entity.getEntityParticipantType().equals(INDIRECT_PARTICIPANT_TYPE)) {
                return isPositiveFields & isValidDetail(DetailsFieldName.DIRECT_PARTICIPANT.fieldName(),
                        entity.getDirectParticipant(),
                        emptyFieldMessage.apply(DetailsFieldName.DIRECT_PARTICIPANT.name(), INDIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty);
            } else if (entity.getEntityParticipantType().equals(DIRECT_PARTICIPANT_TYPE)) {
                return isPositiveFields
                        & isValidDetail(DetailsFieldName.MQ_HOST.fieldName(), entity.getMqHost(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_HOST.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_PORT.fieldName(), entity.getMqPort(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_PORT.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, (Integer port) -> !Optional.ofNullable(port).isPresent())

                        & isValidDetail(DetailsFieldName.MQ_QMANAGER.fieldName(), entity.getMqQManager(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_QMANAGER.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_CHANNEL.fieldName(), entity.getMqChannel(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_CHANNEL.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_QNAME.fieldName(), entity.getMqQueueName(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_QNAME.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_QBINDING.fieldName(), entity.getMqQueueBinding(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_QBINDING.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_QCONTEXT.fieldName(), entity.getMqQueueContext(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_QCONTEXT.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_DEBUG.fieldName(), entity.getMqDebug(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_DEBUG.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_SSLOPTION.fieldName(), entity.getMqSSLOptions(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_SSLOPTION.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_SSLCIPHERS.fieldName(), entity.getMqSSLCiphers(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_SSLCIPHERS.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_SSLSYSTEMCERTID.fieldName(), entity.getMqSSLKeyCert(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_SSLSYSTEMCERTID.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_SSLCACERTID.fieldName(), entity.getMqSSLCaCert(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_SSLCACERTID.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_HEADER.fieldName(), entity.getMqHeader(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_HEADER.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.MQ_SESSIONTIMEOUT.fieldName(), entity.getMqSessionTimeout(),
                        emptyFieldMessage.apply(DetailsFieldName.MQ_SESSIONTIMEOUT.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, (Integer sessionTimeout) ->
                                !Optional.ofNullable(sessionTimeout).isPresent())

                        & isValidDetail(DetailsFieldName.REQUESTORDN.fieldName(), entity.getRequestorDN(),
                        emptyFieldMessage.apply(DetailsFieldName.REQUESTORDN.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.RESPONDERDN.fieldName(), entity.getResponderDN(),
                        emptyFieldMessage.apply(DetailsFieldName.RESPONDERDN.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.REQUESTTYPE.fieldName(), entity.getRequestType(),
                        emptyFieldMessage.apply(DetailsFieldName.REQUESTTYPE.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidDetail(DetailsFieldName.SERVICENAME.fieldName(), entity.getServiceName(),
                        emptyFieldMessage.apply(DetailsFieldName.SERVICENAME.name(), DIRECT_PARTICIPANT_TYPE),
                        constraintValidatorContext, StringUtils::isEmpty);
            }
            return isPositiveFields;
        }
        return false;
    }

    private <T> boolean isValidDetail(String fieldName, T fieldValue, String message,
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
