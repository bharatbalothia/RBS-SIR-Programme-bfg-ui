package com.ibm.sterling.bfg.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.config.ErrorConfig;
import com.ibm.sterling.bfg.app.controller.FileSearchController;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import com.ibm.sterling.bfg.app.model.exception.FileErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice(assignableTypes = FileSearchController.class)
public class FileExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(FileExceptionHandler.class);

    @Autowired
    private ErrorConfig errorConfig;

    @ExceptionHandler(FileTransactionNotFoundException.class)
    public ResponseEntity handleFileTransactionNotFoundException(FileTransactionNotFoundException ex) {
        ErrorMessage error = errorConfig.getErrorMessage(FileErrorCode.FileTransactionNotFoundException);
        String message = Optional.ofNullable(ex.getMessage())
                .map(errMessage -> {
                            String errorMapString = errMessage.substring(errMessage.indexOf("[") + 1, errMessage.indexOf("]"));
                            Map<String, Object> errorMap = new HashMap<>();
                            try {
                                errorMap = new ObjectMapper().readValue(errorMapString, new TypeReference<Map<String, Object>>() {
                                });
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            return (String) errorMap.get("message");
                        }
                ).orElse(null);
        Optional.ofNullable(message).ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
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
                errorConfig.getErrorMessage(FileErrorCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, errors);
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        ErrorMessage errorMessage = errorConfig.getErrorMessage(FileErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION,
                Collections.singletonList(Collections.singletonMap(ex.getName(),
                        ex.getName() + " should be of type " +
                                Objects.requireNonNull(ex.getRequiredType()).getName())));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        String errorName = ex.getClass().getName();
        errorName = errorName.substring(errorName.lastIndexOf(".") + 1);
        LOG.info("File error name: " + errorName);
        String testErrorName = errorName;
        ErrorMessage errorMessage;
        if (Arrays.stream(FileErrorCode.values()).anyMatch(value -> value.name().equals(testErrorName)))
            errorMessage = errorConfig.getErrorMessage(FileErrorCode.valueOf(errorName));
        else errorMessage = errorConfig.getErrorMessage(FileErrorCode.FAIL,
                Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse(ex.getMessage()));
        return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
    }

}
