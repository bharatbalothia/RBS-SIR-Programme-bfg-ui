package com.ibm.sterling.bfg.app.exception.changecontrol;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid user for approval")
public class InvalidUserForApprovalException extends RuntimeException {
}
