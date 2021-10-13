package com.ibm.sterling.bfg.app.exception.entity;

import com.ibm.sterling.bfg.app.controller.EntityController;
import com.ibm.sterling.bfg.app.exception.ErrorMessageHandler;
import com.ibm.sterling.bfg.app.exception.ExceptionDetailsHandler;
import com.ibm.sterling.bfg.app.exception.audit.InvalidEventException;
import com.ibm.sterling.bfg.app.model.exception.EntityErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.*;
import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = EntityController.class)
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(EntityExceptionHandler.class);
    public static final String ENTITY_EXCEPTION = "Handled Entity exception: {} - {}";

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    @Autowired
    private ExceptionDetailsHandler exceptionDetailsHandler;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleRestTemplateException(HttpStatusCodeException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleRestTemplateException(ex);
    }

    @ExceptionHandler(TransmittalException.class)
    public ResponseEntity<Object> handleTransmittalException(TransmittalException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(),
                Optional.ofNullable(ex.getInnerExceptions().get("error"))
                .orElse(ex.getInnerExceptions().get("Execution")));
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                EntityErrorCode.TransmittalException, Collections.singletonList(ex.getInnerExceptions()));
        return new ResponseEntity<>(errorMessage, ex.getHttpStatus());
    }

    @ExceptionHandler(SWIFTNetRoutingRuleException.class)
    public ResponseEntity<Object> handleSWIFTNetRoutingRuleException(SWIFTNetRoutingRuleException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                EntityErrorCode.EntityApprovalException, ex.getErrorMessage(), Collections.singletonList(ex.getInnerExceptions()));
        return new ResponseEntity<>(errorMessage, ex.getHttpStatus());
    }

    @ExceptionHandler(EntityApprovalException.class)
    public ResponseEntity<Object> handleEntityApprovalException(EntityApprovalException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                EntityErrorCode.EntityApprovalException, ex.getErrorMessage(), ex.getErrors());
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<Object> handleInvalidEventException(InvalidEventException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                EntityErrorCode.InvalidEventException, Collections.singletonList(ex.getErrors()));
        return new ResponseEntity<>(errorMessage, ex.getHttpStatus());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleMethodArgumentNotValid(ex, EntityErrorCode.class);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleConstraintViolation(ex, EntityErrorCode.class);
    }

    @ExceptionHandler(FieldsValidationException.class)
    public ResponseEntity<Object> handleValidationException(FieldsValidationException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleFieldsValidation(ex, EntityErrorCode.class);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleMissingServletRequestParameter(ex, EntityErrorCode.class);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleMethodArgumentTypeMismatch(ex, EntityErrorCode.class);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info(ENTITY_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, EntityErrorCode.class);
    }

}
