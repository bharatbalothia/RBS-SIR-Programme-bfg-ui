package com.ibm.sterling.bfg.app.model.validation.unique;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.service.certificate.CertificateIntegrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueBICertificateValidator implements ConstraintValidator<UniqueBICertificate, Object> {
    private static final Logger LOG = LogManager.getLogger(UniqueBICertificateValidator.class);
    @Autowired
    private CertificateIntegrationService certificateIntegrationService;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        LOG.info("CERTIFICATE_NAME validation {} in BI", value);
        boolean isUniqueField;
        try {
            isUniqueField = !Optional.ofNullable(certificateIntegrationService.getCertificateByName(String.valueOf(value)))
                    .isPresent();
        } catch (JsonProcessingException e) {
            LOG.error("CERTIFICATE_NAME validation error: {}", e.getMessage());
            return false;
        }
        LOG.info("Is {} unique for CERTIFICATE_NAME: {}", value, isUniqueField);
        return isUniqueField;
    }

}
