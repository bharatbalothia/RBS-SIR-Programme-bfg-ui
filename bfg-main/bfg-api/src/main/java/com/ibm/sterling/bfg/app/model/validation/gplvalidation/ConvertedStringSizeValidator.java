package com.ibm.sterling.bfg.app.model.validation.gplvalidation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class ConvertedStringSizeValidator implements ConstraintValidator<ConvertedStringSize, List<String>> {

    private static final Logger LOG = LogManager.getLogger(ConvertedStringSizeValidator.class);
    private int maxStringLength;

    @Override
    public void initialize(ConvertedStringSize convertedListSize) {
        maxStringLength = convertedListSize.max();
    }

    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext context) {
        LOG.info("Validating a converted list of strings {}, max string length {}", list, maxStringLength);
        Integer convertedStringLength = Optional.ofNullable(list)
                .map(convertedList -> String.join("|", convertedList).length())
                .orElse(0);
        boolean isValidLength = maxStringLength >= convertedStringLength;
        LOG.info("Is length valid {}", isValidLength);
        return isValidLength;
    }

}
