package com.ibm.sterling.bfg.app.model.exception;

public enum FileErrorCode implements ErrorCode {

    FAIL("FILE_FAIL_CODE", "FILE_FAIL_MSG", "FILE_FAIL_STATUS"),
    RuntimeException("FILE_RTE_CODE", "FILE_RTE_MSG", "FILE_RTE_STATUS"),
    NullPointerException("FILE_NPE_CODE", "FILE_NPE_MSG", "FILE_NPE_STATUS"),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION("FILE_ARG_TYPE_MISMATCH_CODE", "FILE_ARG_TYPE_MISMATCH_MSG", "FILE_ARG_TYPE_MISMATCH_STATUS"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("FILE_NOT_VALID_CODE", "FILE_NOT_VALID_MSG", "FILE_NOT_VALID_STATUS"),
    FileNotFoundException("FILE_NOT_FOUND_CODE", "FILE_NOT_FOUND_MSG", "FILE_NOT_FOUND_STATUS"),
    FileTransactionNotFoundException("FILE_TRANSACT_NOT_FOUND_CODE", "FILE_TRANSACT_NOT_FOUND_MSG", "FILE_TRANSACT_NOT_FOUND_STATUS"),
    AccessDeniedException("FILE_ACCESS_DENIED_CODE", "FILE_ACCESS_DENIED_MSG", "FILE_ACCESS_DENIED_STATUS");

    FileErrorCode(String value, String msg, String status) {
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
