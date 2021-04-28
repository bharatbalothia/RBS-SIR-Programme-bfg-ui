package com.ibm.sterling.bfg.app.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class AuditEvent {
    private String id;
    private String actor;
    private String actionType;
    private String eventContext;
    private String eventType;
    private String objectActedOn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    private String action;

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
