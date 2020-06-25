package com.ibm.sterling.bfg.app.model.exception;

public enum CertificateErrorCode implements ErrorCode {

    FAIL("CER_FAIL_CODE", "CER_FAIL_MSG", "CER_FAIL_STATUS"),
    RuntimeException("CER_RTE_CODE", "CER_RTE_MSG", "CER_RTE_STATUS"),
    NullPointerException("CER_NPE_CODE", "CER_NPE_MSG", "CER_NPE_STATUS"),
    CertificateNotFoundException("CER_NOT_FOUND_CODE", "CER_NOT_FOUND_MSG", "CER_NOT_FOUND_STATUS");

    CertificateErrorCode(String value, String msg, String status) {
        this.val = value;
        this.msg = msg;
        this.status = status;
    }

    private String val;
    private String msg;
    private String status;

    public String val() {
        return val;
    }

    public String msg() {
        return msg;
    }

    public String status() {
        return status;
    }

}
