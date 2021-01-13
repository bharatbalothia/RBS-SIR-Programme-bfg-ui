package com.ibm.sterling.bfg.app.model.validation.file;

import com.ibm.sterling.bfg.app.model.file.SearchCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FromToDateValidator implements ConstraintValidator<FromToDateValid, SearchCriteria> {

    private static final Logger LOG = LogManager.getLogger(FromToDateValidator.class);

    @Override
    public boolean isValid(SearchCriteria searchCriteria, ConstraintValidatorContext context) {
        LOG.info("From and To Date validation");
        return searchCriteria.isDateValid();
    }
}
