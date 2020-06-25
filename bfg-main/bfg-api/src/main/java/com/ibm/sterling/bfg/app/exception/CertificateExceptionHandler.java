package com.ibm.sterling.bfg.app.exception;

import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.controller.CertificateController;
import com.ibm.sterling.bfg.app.model.exception.CertificateErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Optional;

@RestControllerAdvice(assignableTypes = CertificateController.class)
public class CertificateExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(CertificateExceptionHandler.class);

    @Autowired
    private ErrorConfig errorConfig;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity handleRestTemplateException(HttpStatusCodeException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Optional.ofNullable(message)
                        .map(errMessage -> errMessage.substring(message.indexOf("[")))
                        .orElse(message));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> handleAll(Throwable ex) {
        String errorName = ex.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);
        LOG.info("Certificate error name: " + errorName);
        String testErrorName = errorName;
        ErrorMessage errorMessage;
        if (Arrays.stream(CertificateErrorCode.values()).anyMatch(value -> value.name().equals(testErrorName)))
            errorMessage = errorConfig.getErrorMessage(CertificateErrorCode.valueOf(errorName));
        else
            errorMessage = errorConfig.getErrorMessage(CertificateErrorCode.FAIL,
                    Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()));
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

}
