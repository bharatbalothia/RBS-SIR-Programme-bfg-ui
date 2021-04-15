package com.ibm.sterling.bfg.app.exception.changecontrol;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Status is PENDING")
public class StatusPendingException extends RuntimeException {
}