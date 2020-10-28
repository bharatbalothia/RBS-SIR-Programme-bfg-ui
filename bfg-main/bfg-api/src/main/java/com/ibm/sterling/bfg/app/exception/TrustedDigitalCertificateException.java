package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.HttpStatus;

public class TrustedDigitalCertificateException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorMessage;

    public TrustedDigitalCertificateException() {
    }

    public TrustedDigitalCertificateException(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
