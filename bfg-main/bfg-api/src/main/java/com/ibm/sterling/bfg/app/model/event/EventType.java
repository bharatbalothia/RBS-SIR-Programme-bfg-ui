package com.ibm.sterling.bfg.app.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("entity")
    ENTITY("entity"),
    @JsonProperty("trusted-certificate")
    TRUSTED_CERTIFICATE("trusted-certificate"),
    @JsonProperty("transmit")
    TRANSMIT("transmit");

    private String type;

    EventType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
