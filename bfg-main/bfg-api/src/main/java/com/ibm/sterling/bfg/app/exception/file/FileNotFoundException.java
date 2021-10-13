package com.ibm.sterling.bfg.app.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such file")
public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }

}
