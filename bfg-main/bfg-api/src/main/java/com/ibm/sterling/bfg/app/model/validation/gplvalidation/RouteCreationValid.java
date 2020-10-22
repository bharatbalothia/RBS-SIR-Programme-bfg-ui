package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = RouteCreationValidator.class)
@Documented
public @interface RouteCreationValid {

    String message() default "Route fields are not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
