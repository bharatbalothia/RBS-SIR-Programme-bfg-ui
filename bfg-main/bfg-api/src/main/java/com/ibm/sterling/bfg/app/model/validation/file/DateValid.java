package com.ibm.sterling.bfg.app.model.validation.file;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Constraint(validatedBy = DateValidatorUsingDateTimeFormatter.class)
@Retention(RUNTIME)
public @interface DateValid {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern();

}
