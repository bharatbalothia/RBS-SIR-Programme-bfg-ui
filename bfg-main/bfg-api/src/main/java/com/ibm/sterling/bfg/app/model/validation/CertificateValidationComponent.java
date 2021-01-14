package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
public class CertificateValidationComponent {
    @Autowired
    private Validator validator;

    public void validateCertificate(TrustedCertificate trustedCertificate) {
        Set<ConstraintViolation<TrustedCertificate>> violations = validator.validate(trustedCertificate);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
