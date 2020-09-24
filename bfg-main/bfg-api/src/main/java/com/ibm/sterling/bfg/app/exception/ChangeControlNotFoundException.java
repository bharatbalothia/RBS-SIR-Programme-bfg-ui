package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such change control")
public class ChangeControlNotFoundException extends RuntimeException {

    public ChangeControlNotFoundException() {
    }

    public ChangeControlNotFoundException(String message) {
        super(message);
    }

}
