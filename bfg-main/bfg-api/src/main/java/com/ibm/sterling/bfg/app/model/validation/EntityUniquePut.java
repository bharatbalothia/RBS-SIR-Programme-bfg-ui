package com.ibm.sterling.bfg.app.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Constraint(validatedBy = EntityUniqueValidatorPut.class)
@Retention(RUNTIME)
public @interface EntityUniquePut {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends FieldValueExists> service();

    String fieldName();
}
