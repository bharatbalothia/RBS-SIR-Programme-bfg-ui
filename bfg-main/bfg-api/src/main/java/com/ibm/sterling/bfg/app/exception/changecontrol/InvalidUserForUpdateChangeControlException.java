package com.ibm.sterling.bfg.app.exception.changecontrol;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid user for update change control")
public class InvalidUserForUpdateChangeControlException extends RuntimeException {
    public InvalidUserForUpdateChangeControlException() {
    }

    public InvalidUserForUpdateChangeControlException(String message) {
        super(message);
    }
}
