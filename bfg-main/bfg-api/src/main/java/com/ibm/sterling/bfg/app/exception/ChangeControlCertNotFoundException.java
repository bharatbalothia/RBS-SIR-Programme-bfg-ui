package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such change control")
public class ChangeControlCertNotFoundException extends RuntimeException {

    public ChangeControlCertNotFoundException() {
    }

    public ChangeControlCertNotFoundException(String message) {
        super(message);
    }

}
