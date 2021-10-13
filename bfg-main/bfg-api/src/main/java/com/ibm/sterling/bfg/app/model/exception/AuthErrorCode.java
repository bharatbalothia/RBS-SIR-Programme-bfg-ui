package com.ibm.sterling.bfg.app.model.exception;

public enum AuthErrorCode implements ErrorCode {

    FAIL("AUTH_FAIL_CODE", "AUTH_FAIL_MSG", "AUTH_FAIL_STATUS"),
    BadCredentialsException("AUTH_BAD_CRED_CODE", "AUTH_BAD_CRED_MSG", "AUTH_BAD_CRED_STATUS");

    AuthErrorCode(String value, String msg, String status) {
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
