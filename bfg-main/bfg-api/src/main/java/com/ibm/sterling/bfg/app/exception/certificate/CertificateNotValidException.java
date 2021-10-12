package com.ibm.sterling.bfg.app.exception.certificate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "This is a defective certificate")
public class CertificateNotValidException extends RuntimeException {

    public CertificateNotValidException() {
    }

    public CertificateNotValidException(String message) {
        super(message);
    }
}
