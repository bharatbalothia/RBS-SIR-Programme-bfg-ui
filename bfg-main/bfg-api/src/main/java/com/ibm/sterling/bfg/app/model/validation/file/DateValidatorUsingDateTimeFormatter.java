package com.ibm.sterling.bfg.app.model.validation.file;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Optional;

public class DateValidatorUsingDateTimeFormatter implements ConstraintValidator<DateValid, String> {

    private String pattern;

    @Override
    public void initialize(DateValid constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(object)
                .map(date -> {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern)
                            .withResolverStyle(ResolverStyle.SMART);
                    try {
                        dateFormatter.parse(date);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                    return true;
                })
                .orElse(true);
    }

}
