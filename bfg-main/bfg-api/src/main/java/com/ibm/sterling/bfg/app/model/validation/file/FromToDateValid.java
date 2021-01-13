package com.ibm.sterling.bfg.app.model.validation.file;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = FromToDateValidator.class)
@Documented
public @interface FromToDateValid {

    String message() default "From Date and To Date are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
