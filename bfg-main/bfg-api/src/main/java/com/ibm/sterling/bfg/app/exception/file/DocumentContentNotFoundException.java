package com.ibm.sterling.bfg.app.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such document content")
public class DocumentContentNotFoundException extends RuntimeException {

    public DocumentContentNotFoundException() {
    }

    public DocumentContentNotFoundException(String message) {
        super(message);
    }

}
