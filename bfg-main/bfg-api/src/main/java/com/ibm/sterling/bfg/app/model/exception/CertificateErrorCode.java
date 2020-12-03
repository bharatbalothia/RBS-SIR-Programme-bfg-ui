package com.ibm.sterling.bfg.app.model.exception;

public enum CertificateErrorCode implements ErrorCode {

    FAIL("CER_FAIL_CODE", "CER_FAIL_MSG", "CER_FAIL_STATUS"),
    RuntimeException("CER_RTE_CODE", "CER_RTE_MSG", "CER_RTE_STATUS"),
    NullPointerException("CER_NPE_CODE", "CER_NPE_MSG", "CER_NPE_STATUS"),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION("CER_ARG_TYPE_MISMATCH_CODE", "CER_ARG_TYPE_MISMATCH_MSG", "CER_ARG_TYPE_MISMATCH_STATUS"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("CER_NOT_VALID_CODE", "CER_NOT_VALID_MSG", "CER_NOT_VALID_STATUS"),
    METHOD_MISSING_ARGUMENT_EXCEPTION("CER_MISS_REQ_PARAM_CODE", "CER_MISS_REQ_PARAM_MSG", "CER_MISS_REQ_PARAM_STATUS"),
    CertificateNotFoundException("CER_NOT_FOUND_CODE", "CER_NOT_FOUND_MSG", "CER_NOT_FOUND_STATUS"),
    CertificateNotValidException("CER_NOT_VALID_CODE", "CER_NOT_VALID_MSG", "CER_NOT_VALID_STATUS"),
    CertificateApprovalException("CER_APPROVAL_FAIL_CODE", "CER_APPROVAL_FAIL_MSG", "CER_APPROVAL_FAIL_STATUS"),
    FileNotValidException("CER_FILE_NOT_VALID_CODE", "CER_FILE_NOT_VALID_MSG", "CER_FILE_NOT_VALID_STATUS"),
    AccessDeniedException("CER_ACCESS_DENIED_CODE", "CER_ACCESS_DENIED_MSG", "CER_ACCESS_DENIED_STATUS"),
    InvalidUserForApprovalException("CER_INVALID_APPROVE_USER_CODE", "CER_INVALID_APPROVE_USER_MSG", "CER_INVALID_APPROVE_USER_STATUS"),
    StatusNotPendingException("CER_STATUS_NOT_PENDING_CODE", "CER_STATUS_NOT_PENDING_MSG", "CER_STATUS_NOT_PENDING_STATUS"),
    ChangeControlCertNotFoundException("CC_NOT_FOUND_CODE", "CC_NOT_FOUND_MSG", "CC_NOT_FOUND_STATUS"),
    InvalidUserForUpdateChangeControlException("CER_INVALID_EDIT_PENDING_USER_CODE", "CER_INVALID_EDIT_PENDING_USER_MSG", "CER_INVALID_EDIT_PENDING_STATUS");

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
