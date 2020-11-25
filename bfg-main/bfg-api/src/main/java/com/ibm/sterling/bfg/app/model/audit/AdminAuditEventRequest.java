package com.ibm.sterling.bfg.app.model.audit;

public class AdminAuditEventRequest {
    private String actionBy;
    private String actionType;
    private String eventType;
    private String type;
    private String changeID;
    private String actionValue;

    public AdminAuditEventRequest(String actionBy, ActionType actionType, EventType eventType,
                                  Type type, String changeID, String actionValue) {
        this.actionBy = actionBy;
        this.actionType = actionType.attributeName();
        this.eventType = eventType.attributeName();
        this.type = type.attributeName();
        this.changeID = changeID;
        this.actionValue = actionValue;
    }

    public AdminAuditEventRequest() {
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChangeID() {
        return changeID;
    }

    public void setChangeID(String changeID) {
        this.changeID = changeID;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

}
