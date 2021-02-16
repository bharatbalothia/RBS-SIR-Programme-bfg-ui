package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.entity.Schedule;
import com.ibm.sterling.bfg.app.model.validation.Field;
import com.ibm.sterling.bfg.app.model.validation.GenericValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

public class ScheduleValidator extends GenericValidator implements ConstraintValidator<ScheduleValid, Schedule> {

    private static final Logger LOG = LogManager.getLogger(ScheduleValidator.class);
    private static final String EMPTY_STRING_FIELD_FORMAT = "%s cannot be blank because the Type has the value %s";
    private static final String WINDOW = "WINDOW";
    private static final String DAILY = "DAILY";

    @Override
    public boolean isValid(Schedule schedule,
                           ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("Schedule validation");
        Boolean isWindow = schedule.getIsWindow();
        boolean isWindowNotNull = isValidField(
                ScheduleField.ISWINDOW,
                isWindow,
                constraintValidatorContext,
                StringUtils::isEmpty,
                fieldName -> fieldName + " cannot be blank"
        );
        if (isWindowNotNull) {
            if (isWindow) {
                return isValidFieldGroup(
                        new HashMap<Field, String>() {
                            {
                                put(ScheduleField.TIMESTART, schedule.getTimeStart());
                                put(ScheduleField.WINDOWEND, schedule.getWindowEnd());
                                put(ScheduleField.WINDOWINTERVAL, schedule.getWindowInterval());
                            }
                        },
                        constraintValidatorContext,
                        StringUtils::isEmpty,
                        emptyFieldTemplateOnSwitchValue(EMPTY_STRING_FIELD_FORMAT, WINDOW)
                );
            }
            return isValidField(
                    ScheduleField.TIMESTART,
                    schedule.getTimeStart(),
                    constraintValidatorContext,
                    StringUtils::isEmpty,
                    emptyFieldTemplateOnSwitchValue(EMPTY_STRING_FIELD_FORMAT, DAILY)
            );
        }
        return false;
    }

}
