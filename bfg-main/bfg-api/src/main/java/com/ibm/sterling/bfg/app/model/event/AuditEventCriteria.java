package com.ibm.sterling.bfg.app.model.event;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class AuditEventCriteria {
    @JsonProperty("event-type")
    @JsonAlias("eventType")
    private List<EventType> eventType;
    @JsonProperty("username")
    @JsonAlias("userName")
    private String userName;
    @JsonProperty("object-acted-on")
    @JsonAlias("objectActedOn")
    private String objectActedOn;
    private Action action;
    @JsonProperty("action-type")
    @JsonAlias("actionType")
    private ActionType actionType;

    @JsonGetter
    public String getEventType() {
        return Optional.ofNullable(eventType)
                .map(types -> {
                    List<String> eventTypes = types.stream().map(EventType::type).collect(Collectors.toList());
                    return String.join("&event-type=", eventTypes);
                }).orElse(null);
    }

    public void setEventType(List<EventType> eventType) {
        this.eventType = eventType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getObjectActedOn() {
        return objectActedOn;
    }

    public void setObjectActedOn(String objectActedOn) {
        this.objectActedOn = objectActedOn;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
