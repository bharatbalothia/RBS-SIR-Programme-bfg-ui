package com.ibm.sterling.bfg.app.exception;

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

import java.util.Collections;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(GlobalExceptionHandler.class);
    public static final String GLOBAL_EXCEPTION = "Handled Global exception: {} - {}";

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    @Autowired
    private ExceptionDetailsHandler exceptionDetailsHandler;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info(GLOBAL_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, GlobalErrorCode.class);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.info(GLOBAL_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                GlobalErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION,
                Collections.singletonList(
                        Collections.singletonMap(ex.getMethod(),
                                "method is not supported for this request. Supported methods are " +
                                        Objects.requireNonNull(ex.getSupportedHttpMethods())
                        )
                )
        );
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
    }

}
