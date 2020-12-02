package com.ibm.sterling.bfg.app.exception.certificate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "File is not valid")
public class FileNotValidException extends RuntimeException {
}
