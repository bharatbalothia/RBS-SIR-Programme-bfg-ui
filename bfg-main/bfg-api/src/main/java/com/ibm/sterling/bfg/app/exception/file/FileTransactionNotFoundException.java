package com.ibm.sterling.bfg.app.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such file transaction")
public class FileTransactionNotFoundException extends RuntimeException {

    public FileTransactionNotFoundException() {
    }

    public FileTransactionNotFoundException(String message) {
        super(message);
    }

}
