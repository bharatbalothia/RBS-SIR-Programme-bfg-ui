package com.ibm.sterling.bfg.app.model.validation.unique;

import com.ibm.sterling.bfg.app.model.validation.FieldValueExists;

import java.lang.annotation.*;
import javax.validation.*;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Constraint(validatedBy = UniqueValidator.class)
@Retention(RUNTIME)
public @interface Unique {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends FieldValueExists> service();

    String fieldName();
}
