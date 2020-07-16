package com.ibm.sterling.bfg.app.exception;

import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.controller.CertificateController;
import com.ibm.sterling.bfg.app.model.exception.CertificateErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

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

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<Object> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(Collections.singletonMap(error.getField(), error.getDefaultMessage()));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(Collections.singletonMap(error.getObjectName(), error.getDefaultMessage()));
        }
        ErrorMessage errorMessage =
                errorConfig.getErrorMessage(CertificateErrorCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, errors);
        return new ResponseEntity<>(
                errorMessage, errorMessage.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        List<Object> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyName;
            String propertyPath = violation.getPropertyPath().toString();
            if (StringUtils.isEmpty(propertyPath))
                propertyName = "entity";
            else propertyName = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
            errors.add(Collections.singletonMap(propertyName, violation.getMessage()));
        }
        ErrorMessage errorMessage =
                errorConfig.getErrorMessage(CertificateErrorCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, errors);
        return new ResponseEntity<>(
                errorMessage, errorMessage.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ErrorMessage errorMessage =
                errorConfig.getErrorMessage(CertificateErrorCode.METHOD_MISSING_ARGUMENT_EXCEPTION,
                        Collections.singletonList(Collections.singletonMap(ex.getParameterName(), ex.getParameterName() + " parameter is missing")));
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        ErrorMessage errorMessage =
                errorConfig.getErrorMessage(CertificateErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION,
                        Collections.singletonList(Collections.singletonMap(ex.getName(),
                                ex.getName() + " should be of type " +
                                        Objects.requireNonNull(ex.getRequiredType()).getName())));
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
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
