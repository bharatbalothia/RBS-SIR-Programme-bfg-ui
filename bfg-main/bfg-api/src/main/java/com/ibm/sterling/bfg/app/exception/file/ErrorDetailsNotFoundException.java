package com.ibm.sterling.bfg.app.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ErrorDetailsNotFoundException extends RuntimeException {
}
