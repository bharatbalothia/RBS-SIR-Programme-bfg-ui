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

    @Autowired
    private ErrorMessageHandler errorMessageHandler;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ExceptionDetailsHandler exceptionDetailsHandler;

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity handleRestTemplateException(HttpStatusCodeException ex) {
        return exceptionDetailsHandler.handleRestTemplateException(ex);
    }

    @ExceptionHandler(DocumentContentNotFoundException.class)
    public ResponseEntity handleDocumentContentNotFoundException(DocumentContentNotFoundException ex) {
        ErrorMessage error = errorMessageHandler.getErrorMessage(FileErrorCode.DocumentContentNotFoundException);
        Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "errorDescription"))
                .ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(FileTransactionNotFoundException.class)
    public ResponseEntity handleFileTransactionNotFoundException(FileTransactionNotFoundException ex) {
        ErrorMessage error = errorMessageHandler.getErrorMessage(FileErrorCode.FileTransactionNotFoundException);
        Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "message"))
                .ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleFileNotFoundException(FileNotFoundException ex) {
        ErrorMessage error = errorMessageHandler.getErrorMessage(FileErrorCode.FileNotFoundException);
        Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "message"))
                .ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        return exceptionDetailsHandler.handleMethodArgumentNotValid(ex, FileErrorCode.class);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return exceptionDetailsHandler.handleMethodArgumentTypeMismatch(ex, FileErrorCode.class);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info("File exception: " + ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, FileErrorCode.class);
    }

}
