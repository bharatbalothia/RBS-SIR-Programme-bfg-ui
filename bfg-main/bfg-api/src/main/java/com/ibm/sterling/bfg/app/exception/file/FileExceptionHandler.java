package com.ibm.sterling.bfg.app.exception.file;

import com.ibm.sterling.bfg.app.exception.ErrorMessageHandler;
import com.ibm.sterling.bfg.app.exception.ExceptionDetailsHandler;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.controller.FileSearchController;
import com.ibm.sterling.bfg.app.controller.SCTTransactionSearchController;
import com.ibm.sterling.bfg.app.controller.WorkflowController;
import com.ibm.sterling.bfg.app.model.exception.ErrorMessage;
import com.ibm.sterling.bfg.app.model.exception.FileErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = {FileSearchController.class, SCTTransactionSearchController.class, WorkflowController.class})
public class FileExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(FileExceptionHandler.class);
    public static final String FILE_EXCEPTION = "Handled File exception: {} - {}";

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ExceptionDetailsHandler exceptionDetailsHandler;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> handleRestTemplateException(HttpStatusCodeException ex) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleRestTemplateException(ex);
    }

    @ExceptionHandler(DocumentContentNotFoundException.class)
    public ResponseEntity<Object> handleDocumentContentNotFoundException(DocumentContentNotFoundException ex) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage error = errorMessageHandler.getErrorMessage(FileErrorCode.DocumentContentNotFoundException);
        Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "errorDescription"))
                .ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(FileTransactionNotFoundException.class)
    public ResponseEntity<Object> handleFileTransactionNotFoundException(FileTransactionNotFoundException ex) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage error = errorMessageHandler.getErrorMessage(FileErrorCode.FileTransactionNotFoundException);
        Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "message"))
                .ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        ErrorMessage error = errorMessageHandler.getErrorMessage(FileErrorCode.FileNotFoundException);
        Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "message"))
                .ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleMethodArgumentNotValid(ex, FileErrorCode.class);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleMethodArgumentTypeMismatch(ex, FileErrorCode.class);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info(FILE_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, FileErrorCode.class);
    }

}
