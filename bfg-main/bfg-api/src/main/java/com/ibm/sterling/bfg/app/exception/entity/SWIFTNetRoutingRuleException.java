package com.ibm.sterling.bfg.app.exception.entity;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class SWIFTNetRoutingRuleException extends RuntimeException {
    private Map<String, List<Object>> innerExceptions;
    private HttpStatus httpStatus;
    private String errorMessage;

    public SWIFTNetRoutingRuleException(Map<String, List<Object>> innerExceptions, HttpStatus httpStatus, String errorMessage) {
        this.innerExceptions = innerExceptions;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public Map<String, List<Object>> getInnerExceptions() {
        return innerExceptions;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
