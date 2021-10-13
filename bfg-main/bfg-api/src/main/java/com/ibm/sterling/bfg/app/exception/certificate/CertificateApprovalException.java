package com.ibm.sterling.bfg.app.exception.certificate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error when approving the certificate")
public class CertificateApprovalException extends RuntimeException {

    private String approvalErrorMessage;

    public CertificateApprovalException() {
    }

    public CertificateApprovalException(String integratorErrorMessage, String approvalErrorMessage) {
        super(integratorErrorMessage);
        this.approvalErrorMessage = approvalErrorMessage;
    }

    public String getApprovalErrorMessage() {
        return approvalErrorMessage;
    }

}
