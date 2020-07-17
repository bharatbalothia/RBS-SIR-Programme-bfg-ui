package com.ibm.sterling.bfg.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "File type is not valid")
public class FileTypeNotValidException extends RuntimeException {
}
