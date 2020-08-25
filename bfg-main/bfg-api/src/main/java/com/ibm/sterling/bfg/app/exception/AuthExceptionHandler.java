package com.ibm.sterling.bfg.app.exception;

import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.controller.AuthController;
import com.ibm.sterling.bfg.app.model.exception.AuthErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Optional;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(AuthExceptionHandler.class);

    @Autowired
    private ErrorConfig errorConfig;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        String errorName = ex.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);
        LOG.info("Authentication error name: " + errorName);
        final String testErrorName = errorName;
        ErrorMessage errorMessage;
        if (Arrays.stream(AuthErrorCode.values()).anyMatch(value -> value.name().equals(testErrorName)))
            errorMessage = errorConfig.getErrorMessage(AuthErrorCode.valueOf(errorName));
        else {
            errorMessage = errorConfig.getErrorMessage(AuthErrorCode.FAIL,
                    Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()), null);
        }
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

}
