package com.ibm.sterling.bfg.app.model.validation.unique;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Constraint(validatedBy = UniqueBICertificateValidator.class)
@Retention(RUNTIME)
public @interface UniqueBICertificate {

    String message() default "Trusted certificate with this CERTIFICATE_NAME already exists in BI";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
