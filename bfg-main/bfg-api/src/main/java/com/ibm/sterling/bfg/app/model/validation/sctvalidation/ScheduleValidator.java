package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.entity.Schedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Formatter;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ScheduleValidator implements ConstraintValidator<ScheduleValid, Schedule> {

    private static final Logger LOG = LogManager.getLogger(ScheduleValidator.class);

    private static final String WINDOW = "WINDOW";
    private static final String DAILY = "DAILY";

    @Override
    public boolean isValid(Schedule schedule,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Schedule validation");
        BiFunction<String, String, String> emptyFieldMessage =
                (fieldName, directParticipant) ->
                        new Formatter().format("%s cannot be empty because the Type has the value %s",
                                fieldName, directParticipant).toString();

        Boolean isWindow = schedule.getIsWindow();
        boolean isWindowNotNull = isValidSchedule(ScheduleFieldName.ISWINDOW.fieldName(), isWindow,
                ScheduleFieldName.ISWINDOW.name() + " cannot be empty",
                constraintValidatorContext, StringUtils::isEmpty);

        if (isWindowNotNull) {
            if (isWindow) {
                return isValidSchedule(ScheduleFieldName.TIMESTART.fieldName(), schedule.getTimeStart(),
                        emptyFieldMessage.apply(ScheduleFieldName.TIMESTART.name(), WINDOW),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidSchedule(ScheduleFieldName.WINDOWEND.fieldName(), schedule.getWindowEnd(),
                        emptyFieldMessage.apply(ScheduleFieldName.WINDOWEND.name(), WINDOW),
                        constraintValidatorContext, StringUtils::isEmpty)

                        & isValidSchedule(ScheduleFieldName.WINDOWINTERVAL.fieldName(), schedule.getWindowInterval(),
                        emptyFieldMessage.apply(ScheduleFieldName.WINDOWINTERVAL.name(), WINDOW),
                        constraintValidatorContext, StringUtils::isEmpty);
            }
            return isValidSchedule(ScheduleFieldName.TIMESTART.fieldName(), schedule.getTimeStart(),
                    emptyFieldMessage.apply(ScheduleFieldName.TIMESTART.name(), DAILY),
                    constraintValidatorContext, StringUtils::isEmpty);
        }
        return false;
    }

    private <T> boolean isValidSchedule(String fieldName, T fieldValue, String message,
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
