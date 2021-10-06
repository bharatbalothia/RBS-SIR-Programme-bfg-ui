package com.ibm.sterling.bfg.app.exception.certificate;

public class CertificateIntegrationException extends RuntimeException {

    private String errorMessage;

    public CertificateIntegrationException() {
    }

    public CertificateIntegrationException(String integratorErrorMessage, String message) {
        super(integratorErrorMessage);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
