package com.ibm.sterling.bfg.app.exception.entity;

public class FieldsValidationException extends RuntimeException {

    private String errorCause;

    public FieldsValidationException(String message, String errorCause) {
        super(message);
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
