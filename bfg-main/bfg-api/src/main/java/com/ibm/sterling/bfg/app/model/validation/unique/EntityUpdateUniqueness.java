package com.ibm.sterling.bfg.app.model.validation.unique;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = EntityUpdateUniquenessValidator.class)
public @interface EntityUpdateUniqueness {

    String message() default "entity has to be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
