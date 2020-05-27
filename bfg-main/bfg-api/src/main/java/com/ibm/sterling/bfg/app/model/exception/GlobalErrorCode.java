package com.ibm.sterling.bfg.app.model.exception;

public enum GlobalErrorCode implements ErrorCode {

    FAIL("FAIL_CODE", "FAIL_MSG", "FAIL_STATUS"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION("REQ_METHOD_NOT_SUP_CODE", "REQ_METHOD_NOT_SUP_MSG", "REQ_METHOD_NOT_SUP_STATUS"),
    ResourceNotFoundException("RES_NOT_FOUND_CODE", "RES_NOT_FOUND_MSG", "RES_NOT_FOUND_STATUS");

    GlobalErrorCode(String value, String msg, String status) {
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


