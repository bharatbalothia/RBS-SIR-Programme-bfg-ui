package com.ibm.sterling.bfg.app.exception.audit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid user for event log")
public class InvalidUserForEventLogAccessException extends RuntimeException{

    public InvalidUserForEventLogAccessException() {
    }

    public InvalidUserForEventLogAccessException(String message) {
        super(message);
    }
}
