package com.ibm.sterling.bfg.app.model.exception;

public enum EventErrorCode implements ErrorCode {

    InvalidUserForEventLogAccessException("EVENT_ACCESS_DENIED_CODE", "EVENT_ACCESS_DENIED_MSG", "EVENT_ACCESS_DENIED_STATUS");

    EventErrorCode(String value, String msg, String status) {
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
