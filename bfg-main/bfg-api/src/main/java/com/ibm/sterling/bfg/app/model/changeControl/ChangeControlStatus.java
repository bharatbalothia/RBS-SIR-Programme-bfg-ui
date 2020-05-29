package com.ibm.sterling.bfg.app.model.changeControl;

public enum ChangeControlStatus {
    PENDING(0, "Pending"),
    REJECTED(1, "Rejected"),
    ACCEPTED(2, "Accepted"),
    FAILED(-1, "Failed"),
    UNKNOWN(-2, "Unknown");

    private final int STATUS;
    private final String STATUS_TEXT;

    ChangeControlStatus(int status, String status_text) {
        STATUS = status;
        STATUS_TEXT = status_text;
    }

    public String getStatusText() { return STATUS_TEXT; }

    public int getStatusValue() {
        return STATUS;
    }
}
