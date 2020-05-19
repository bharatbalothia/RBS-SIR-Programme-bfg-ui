package com.ibm.sterling.bfg.app.exception;

import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import com.ibm.sterling.bfg.app.model.exception.GlobalErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ErrorConfig errorConfig;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        String errorName = ex.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);
        LOG.info("Global error name: " + errorName);
        final String testErrorName = errorName;
        ErrorMessage errorMessage;
        if (Arrays.stream(GlobalErrorCode.values()).anyMatch(value -> value.name().equals(testErrorName)))
            errorMessage = errorConfig.getErrorMessage(GlobalErrorCode.valueOf(errorName));
        else {
            errorMessage = errorConfig.getErrorMessage(GlobalErrorCode.FAIL, Optional.ofNullable(ex.getCause()).map(Throwable::getLocalizedMessage).orElse(ex.getLocalizedMessage()));
        }
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorMessage errorMessage = errorConfig.getErrorMessage(
                GlobalErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION,
                Collections.singletonMap(ex.getMethod(),
                        "method is not supported for this request. Supported methods are " +
                                Objects.requireNonNull(ex.getSupportedHttpMethods())
                )
        );
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
    }

}
