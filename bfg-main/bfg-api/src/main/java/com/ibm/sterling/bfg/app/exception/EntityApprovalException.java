package com.ibm.sterling.bfg.app.exception;

import java.util.List;
import java.util.Map;

public class EntityApprovalException extends RuntimeException {
    private List<Map<String, String>> errors;
    private String errorMessage;

    public EntityApprovalException(List<Map<String, String>> errors, String errorMessage) {
        this.errors = errors;
        this.errorMessage = errorMessage;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
