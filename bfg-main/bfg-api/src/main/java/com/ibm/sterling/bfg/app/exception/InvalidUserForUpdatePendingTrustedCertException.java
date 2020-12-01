package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid user for update pending trusted certificate")
public class InvalidUserForUpdatePendingTrustedCertException extends RuntimeException {
}
