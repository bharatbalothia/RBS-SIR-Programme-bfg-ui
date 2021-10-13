package com.ibm.sterling.bfg.app.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Action {
    @JsonProperty("approved")
    APPROVED,
    @JsonProperty("rejected")
    REJECTED,
    @JsonProperty("requested")
    REQUESTED,
    @JsonProperty("request-edited")
    REQUEST_EDITED,
    @JsonProperty("request-cancelled")
    REQUEST_CANCELLED;
}
