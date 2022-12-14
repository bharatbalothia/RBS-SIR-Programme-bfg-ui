package com.ibm.sterling.bfg.app.model.audit;

public enum EventType {
    ACCEPTED("approved"),
    REJECTED("rejected"),
    PENDING("requested"),
    REQUEST_EDITED("request-edited"),
    REQUEST_CANCELLED("request-cancelled");

    EventType(String attributeName) {
        this.attributeName = attributeName;
    }

    private String attributeName;

    public String attributeName() {
        return attributeName;
    }

}
