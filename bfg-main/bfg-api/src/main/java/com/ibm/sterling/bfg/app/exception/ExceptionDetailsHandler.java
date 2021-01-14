package com.ibm.sterling.bfg.app.exception;

import com.ibm.sterling.bfg.app.exception.entity.FieldsValidationException;
import com.ibm.sterling.bfg.app.model.exception.ErrorCode;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@Component
public class ExceptionDetailsHandler {

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    public ResponseEntity handleRestTemplateException(HttpStatusCodeException ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Optional.ofNullable(message)
                        .map(errorMessage -> {
                                    int errorStartIndex = errorMessage.indexOf("[");
                                    int errorEndIndex = errorMessage.lastIndexOf("]");
                                    return errorStartIndex == -1 || errorEndIndex == -1 ? errorMessage :
                                            errorMessage.substring(errorStartIndex, errorEndIndex + 1);
                                }
                        ).orElse(message));
    }

    public <E extends Enum<E> & ErrorCode> ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, Class<E> errorCode) {
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                Enum.valueOf(errorCode, "METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION"),
                Collections.singletonList(Collections.singletonMap(ex.getName(),
                        ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName()))
        );
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
    }

    public <E extends Enum<E> & ErrorCode> ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, Class<E> errorCode) {
        List<Object> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(Collections.singletonMap(violation.getPropertyPath(), violation.getMessage()));
        }
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                Enum.valueOf(errorCode, "METHOD_ARGUMENT_NOT_VALID_EXCEPTION"), errors);
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    public <E extends Enum<E> & ErrorCode> ResponseEntity<Object> handleFieldsValidation(
            FieldsValidationException ex, Class<E> errorCode) {
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                Enum.valueOf(errorCode, "METHOD_ARGUMENT_NOT_VALID_EXCEPTION"),
                Collections.singletonList(Collections.singletonMap(ex.getErrorCause(), ex.getMessage())));
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    public <E extends Enum<E> & ErrorCode> ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, Class<E> errorCode) {
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                Enum.valueOf(errorCode, "METHOD_MISSING_ARGUMENT_EXCEPTION"),
                Collections.singletonList(Collections.singletonMap(ex.getParameterName(), ex.getParameterName() + " parameter is missing")));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
    }

    public <E extends Enum<E> & ErrorCode> ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, Class<E> errorCode) {
        List<Object> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(Collections.singletonMap(error.getField(), error.getDefaultMessage()));
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(Collections.singletonMap(error.getObjectName(), error.getDefaultMessage()));
        }
        ErrorMessage errorMessage = errorMessageHandler.getErrorMessage(
                Enum.valueOf(errorCode, "METHOD_ARGUMENT_NOT_VALID_EXCEPTION"), errors);
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    public <E extends Enum<E> & ErrorCode> ResponseEntity<Object> handleAll(Throwable ex, Class<E> errorCode) {
        String errorName = ex.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);
        String testErrorName = errorName;
        ErrorMessage errorMessage;
        if (Arrays.stream(errorCode.getEnumConstants()).anyMatch(value -> value.name().equals(testErrorName)))
            errorMessage = errorMessageHandler.getErrorMessage(Enum.valueOf(errorCode, errorName));
        else
            errorMessage = errorMessageHandler.getErrorMessage(
                    Enum.valueOf(errorCode, "FAIL"),
                    Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()),
                    null
            );
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

}
