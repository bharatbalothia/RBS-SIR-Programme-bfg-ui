package com.ibm.sterling.bfg.app.exception.certificate;

import com.ibm.sterling.bfg.app.exception.ErrorMessageHandler;
import com.ibm.sterling.bfg.app.exception.ExceptionDetailsHandler;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.controller.CertificateController;
import com.ibm.sterling.bfg.app.model.exception.CertificateErrorCode;
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

import javax.validation.ConstraintViolationException;
import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = CertificateController.class)
public class CertificateExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LogManager.getLogger(CertificateExceptionHandler.class);
    private static final String CONTACT_MESSAGE = "Please contact application support.";

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

    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity handleEntityApprovalException(CertificateNotFoundException ex) {
        ErrorMessage error = errorMessageHandler.getErrorMessage(CertificateErrorCode.CertificateNotFoundException);
        Optional.ofNullable(ex.getMessage()).ifPresent(error::setMessage);
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(CertificateApprovalException.class)
    public ResponseEntity handleCertificateApprovalException(CertificateApprovalException ex) {
        ErrorMessage error = errorMessageHandler.getErrorMessage(CertificateErrorCode.CertificateApprovalException);
        error.setMessage(ex.getApprovalErrorMessage() + " - " +
                Optional.ofNullable(apiDetailsHandler.extractErrorMessage(ex.getMessage(), "errorDescription"))
                        .orElseGet(ex::getMessage) + ". " +
                CONTACT_MESSAGE
        );
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        return exceptionDetailsHandler.handleMethodArgumentNotValid(ex, CertificateErrorCode.class);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return exceptionDetailsHandler.handleConstraintViolation(ex, CertificateErrorCode.class);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        return exceptionDetailsHandler.handleMissingServletRequestParameter(ex, CertificateErrorCode.class);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return exceptionDetailsHandler.handleMethodArgumentTypeMismatch(ex, CertificateErrorCode.class);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleAll(Throwable ex) {
        LOG.info("Certificate exception: " + ex.getMessage());
        return exceptionDetailsHandler.handleAll(ex, CertificateErrorCode.class);
    }

}
