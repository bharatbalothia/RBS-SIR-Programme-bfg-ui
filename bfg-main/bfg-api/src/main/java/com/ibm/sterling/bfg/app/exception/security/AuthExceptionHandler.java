package com.ibm.sterling.bfg.app.exception.security;

import com.ibm.sterling.bfg.app.controller.AuthController;
import com.ibm.sterling.bfg.app.exception.ErrorMessageHandler;
import com.ibm.sterling.bfg.app.exception.ExceptionDetailsHandler;
import com.ibm.sterling.bfg.app.model.exception.AuthErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(AuthExceptionHandler.class);
    public static final String AUTHENTICATION_EXCEPTION = "Handled Authentication exception: {} - {}";

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    @Autowired
    private ExceptionDetailsHandler exceptionDetailsHandler;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleRestTemplateException(HttpStatusCodeException ex) {
        LOG.info(AUTHENTICATION_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleRestTemplateException(ex);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Object> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        LOG.info(AUTHENTICATION_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage error = errorMessageHandler.getErrorMessage(AuthErrorCode.BadCredentialsException);
        Optional.ofNullable(ex.getMessage()).ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info(AUTHENTICATION_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, AuthErrorCode.class);
    }

}
