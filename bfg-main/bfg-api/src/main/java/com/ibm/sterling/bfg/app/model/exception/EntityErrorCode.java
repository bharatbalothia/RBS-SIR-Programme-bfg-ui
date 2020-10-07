package com.ibm.sterling.bfg.app.model.exception;

public enum EntityErrorCode implements ErrorCode {

    FAIL("ENT_FAIL_CODE", "ENT_FAIL_MSG", "ENT_FAIL_STATUS"),
    RuntimeException("ENT_RTE_CODE", "ENT_RTE_MSG", "ENT_RTE_STATUS"),
    NullPointerException("ENT_NPE_CODE", "ENT_NPE_MSG", "ENT_NPE_STATUS"),
    EntityNotFoundException("ENT_NOT_FOUND_CODE", "ENT_NOT_FOUND_MSG", "ENT_NOT_FOUND_STATUS"),
    ChangeControlNotFoundException("CC_NOT_FOUND_CODE", "CC_NOT_FOUND_MSG", "CC_NOT_FOUND_STATUS"),
    AccessDeniedException("ENT_ACCESS_DENIED_CODE", "ENT_ACCESS_DENIED_MSG", "ENT_ACCESS_DENIED_STATUS"),
    InvalidUserForApprovalException("ENT_INVALID_APPROVE_USER_CODE", "ENT_INVALID_APPROVE_USER_MSG", "ENT_INVALID_APPROVE_USER_STATUS"),
    InvalidUserForUpdatePendingEntityException("ENT_INVALID_EDIT_PENDING_USER_CODE", "ENT_INVALID_EDIT_PENDING_USER_MSG", "ENT_INVALID_EDIT_PENDING_STATUS"),
    StatusNotPendingException("ENT_STATUS_NOT_PENDING_CODE", "ENT_STATUS_NOT_PENDING_MSG", "ENT_STATUS_NOT_PENDING_STATUS"),
    TransmittalException("ENT_TRANSMIT_FAIL_CODE", "ENT_TRANSMIT_FAIL_MSG", "ENT_TRANSMIT_FAIL_STATUS"),
    SWIFTNetRoutingRuleException("ENT_ROUTING_RULE_FAIL_CODE", "ENT_ROUTING_RULE_FAIL_MSG", "ENT_ROUTING_RULE_FAIL_STATUS"),
    EntityApprovalException("ENT_APPROVAL_FAIL_CODE", "ENT_APPROVAL_FAIL_MSG", "ENT_APPROVAL_FAIL_STATUS"),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION("ENT_ARG_TYPE_MISMATCH_CODE", "ENT_ARG_TYPE_MISMATCH_MSG", "ENT_ARG_TYPE_MISMATCH_STATUS"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("ENT_NOT_VALID_CODE", "ENT_NOT_VALID_MSG", "ENT_NOT_VALID_STATUS"),
    METHOD_MISSING_ARGUMENT_EXCEPTION("ENT_MISS_REQ_PARAM_CODE", "ENT_MISS_REQ_PARAM_MSG", "ENT_MISS_REQ_PARAM_STATUS");

    EntityErrorCode(String value, String msg, String status) {
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
