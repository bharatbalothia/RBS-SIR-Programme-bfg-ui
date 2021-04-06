package com.ibm.sterling.bfg.app.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ActionType {
    @JsonProperty("create")
    CREATE,
    @JsonProperty("update")
    UPDATE,
    @JsonProperty("delete")
    DELETE
}
