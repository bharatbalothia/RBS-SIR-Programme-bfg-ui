package com.ibm.sterling.bfg.app.exception.audit;

import com.ibm.sterling.bfg.app.controller.AuditEventController;
import com.ibm.sterling.bfg.app.exception.ErrorMessageHandler;
import com.ibm.sterling.bfg.app.exception.ExceptionDetailsHandler;
import com.ibm.sterling.bfg.app.model.exception.EntityErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import com.ibm.sterling.bfg.app.model.exception.EventErrorCode;
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
@RestControllerAdvice(assignableTypes = {AuditEventController.class})
public class EventExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(EventExceptionHandler.class);
    public static final String EVENT_EXCEPTION = "Handled Event exception: {} - {}";

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    @Autowired
    private ExceptionDetailsHandler exceptionDetailsHandler;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleRestTemplateException(HttpStatusCodeException ex) {
        LOG.info(EVENT_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleRestTemplateException(ex);
    }

    @ExceptionHandler(InvalidUserForEventLogAccessException.class)
    public ResponseEntity<Object> handleInvalidUserForEventLogAccessExceptionException(InvalidUserForEventLogAccessException ex) {
        LOG.info(EVENT_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                EventErrorCode.InvalidUserForEventLogAccessException);
        Optional.ofNullable(ex.getMessage()).ifPresent(errorMessage::setMessage);
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info(EVENT_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, EntityErrorCode.class);
    }

}
