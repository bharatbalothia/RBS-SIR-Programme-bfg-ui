package com.ibm.sterling.bfg.app.exception.certificate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "There is no such trusted certificate")
public class CertificateNotFoundException extends RuntimeException {

    public CertificateNotFoundException() {
    }

    public CertificateNotFoundException(String message) {
        super(message);
    }

}
