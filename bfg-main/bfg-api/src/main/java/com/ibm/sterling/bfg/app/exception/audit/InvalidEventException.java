package com.ibm.sterling.bfg.app.exception.audit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Invalid event")
public class InvalidEventException extends RuntimeException {
    private Map<String, List<Object>> errors;
    private HttpStatus httpStatus;

    public InvalidEventException(Map<String, List<Object>> errors, HttpStatus httpStatus) {
        this.errors = errors;
        this.httpStatus = httpStatus;
    }

    public Map<String, List<Object>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<Object>> errors) {
        this.errors = errors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
