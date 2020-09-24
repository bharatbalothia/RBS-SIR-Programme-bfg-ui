package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class TransmittalException extends RuntimeException {
    private Map<String, List<Object>> innerExceptions;
    private HttpStatus httpStatus;

    public TransmittalException(Map<String, List<Object>> innerExceptions, HttpStatus httpStatus) {
        this.innerExceptions = innerExceptions;
        this.httpStatus = httpStatus;
    }

    public Map<String, List<Object>> getInnerExceptions() {
        return innerExceptions;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
