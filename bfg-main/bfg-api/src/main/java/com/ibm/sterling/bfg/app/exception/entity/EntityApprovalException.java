package com.ibm.sterling.bfg.app.exception.entity;

import java.util.List;

public class EntityApprovalException extends RuntimeException {
    private List<Object> errors;
    private String errorMessage;

    public EntityApprovalException(List<Object> errors, String errorMessage) {
        this.errors = errors;
        this.errorMessage = errorMessage;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
