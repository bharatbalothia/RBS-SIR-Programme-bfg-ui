package com.ibm.sterling.bfg.app.model.audit;

import com.ibm.sterling.bfg.app.model.certificate.ChangeControlCert;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;

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

    public AdminAuditEventRequest(ChangeControl changeControl, String actionBy) {
        this(
                actionBy,
                ActionType.valueOf(changeControl.getOperation().name()),
                EventType.valueOf(changeControl.getStatus().name()),
                Type.ENTITY,
                changeControl.getChangeID(),
                changeControl.getResultMeta1()
        );
    }

    public AdminAuditEventRequest(ChangeControl changeControl, EventType eventType, String actionValue) {
        this(
                changeControl.getChanger(),
                ActionType.valueOf(changeControl.getOperation().name()),
                eventType,
                Type.ENTITY,
                changeControl.getChangeID(),
                actionValue
        );
    }

    public AdminAuditEventRequest(ChangeControlCert changeControlCert, String actionBy) {
        this(
                actionBy,
                ActionType.valueOf(changeControlCert.getOperation().name()),
                EventType.valueOf(changeControlCert.getStatus().name()),
                Type.TRUSTED_CERTIFICATE,
                changeControlCert.getChangeID(),
                changeControlCert.getResultMeta1()
        );
    }

    public AdminAuditEventRequest(ChangeControlCert changeControlCert, EventType eventType, String actionValue) {
        this(
                changeControlCert.getChanger(),
                ActionType.valueOf(changeControlCert.getOperation().name()),
                eventType,
                Type.TRUSTED_CERTIFICATE,
                changeControlCert.getChangeID(),
                actionValue
        );
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
