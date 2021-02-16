package com.ibm.sterling.bfg.app.model.validation;

import javax.validation.ConstraintValidatorContext;
import java.util.Formatter;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class GenericValidator {

    public Function<Field, String> emptyFieldTemplateOnSwitchValue(String emptyFieldFormat, String type) {
        return fieldName -> new Formatter().format(emptyFieldFormat, fieldName, type).toString();
    }

    public <T> boolean isValidFieldGroup(Map<Field, T> fields,
                                         ConstraintValidatorContext constraintValidatorContext,
                                         Predicate<T> validation, Function<Field, String> template) {
        return fields.entrySet().stream()
                .map(field -> validate(
                        field.getKey().fieldName(),
                        field.getValue(),
                        constraintValidatorContext, validation, template.apply(field.getKey())
                )).reduce(Boolean.TRUE, Boolean::logicalAnd);
    }

    public <T> boolean isValidField(Field field, T fieldValue,
                                    ConstraintValidatorContext constraintValidatorContext,
                                    Predicate<T> validation, Function<Field, String> template) {
        return validate(
                field.fieldName(),
                fieldValue,
                constraintValidatorContext,
                validation,
                template.apply(field)
        );
    }

    public <T> boolean validate(String fieldName, T fieldValue,
                                ConstraintValidatorContext constraintValidatorContext,
                                Predicate<T> validation, String message) {
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
