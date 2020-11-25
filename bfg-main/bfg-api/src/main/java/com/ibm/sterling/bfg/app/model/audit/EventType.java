package com.ibm.sterling.bfg.app.model.audit;

public enum EventType {
    APPROVED("approved"),
    REJECTED("rejected"),
    REQUESTED("requested");

    EventType(String attributeName) {
        this.attributeName = attributeName;
    }

    private String attributeName;

    public String attributeName() {
        return attributeName;
    }

}
