package com.ibm.sterling.bfg.app.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class AuditEvent {
    private String id;
    private String actor;
    private ActionType actionType;
    private String eventContext;
    private String eventType;
    private String objectActedOn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    private Action action;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getEventContext() {
        return eventContext;
    }

    public void setEventContext(String eventContext) {
        this.eventContext = eventContext;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getObjectActedOn() {
        return objectActedOn;
    }

    public void setObjectActedOn(String objectActedOn) {
        this.objectActedOn = objectActedOn;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
