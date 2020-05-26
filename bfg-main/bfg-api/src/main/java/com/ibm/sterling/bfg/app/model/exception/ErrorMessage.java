package com.ibm.sterling.bfg.app.model.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ErrorMessage {

    private String code;
    private String message;
    private HttpStatus httpStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> errors;

    public ErrorMessage(Environment environment, ErrorCode code) {
        this.setCode(environment.getProperty(code.val()));
        this.setMessage(environment.getProperty(code.msg()));
        this.setHttpStatus(HttpStatus.valueOf(environment.getProperty(code.status())));
    }

    public ErrorMessage(Environment environment, ErrorCode code, String message) {
        this.setCode(environment.getProperty(code.val()));
        this.setMessage(message);
        this.setHttpStatus(HttpStatus.valueOf(environment.getProperty(code.status())));
    }

    public ErrorMessage(Environment environment, ErrorCode code, List<Object> data) {
        this.setCode(environment.getProperty(code.val()));
        this.setMessage(environment.getProperty(code.msg()));
        this.setHttpStatus(HttpStatus.valueOf(environment.getProperty(code.status())));
        this.setErrors(data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
