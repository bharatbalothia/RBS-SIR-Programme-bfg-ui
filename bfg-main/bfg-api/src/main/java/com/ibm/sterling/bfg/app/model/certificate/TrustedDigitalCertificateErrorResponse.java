package com.ibm.sterling.bfg.app.model.certificate;

public class TrustedDigitalCertificateErrorResponse {
    private Integer errorCode;
    private String errorDescription;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
