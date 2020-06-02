package com.ibm.sterling.bfg.app.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = EntityUniqueValidationMailboxPathOutAndMqQueueOut.class)
public @interface MailboxMqQueueOutValid {
    String message() default "MAILBOXPATHOUT or MQQUEUEOUT has to be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
