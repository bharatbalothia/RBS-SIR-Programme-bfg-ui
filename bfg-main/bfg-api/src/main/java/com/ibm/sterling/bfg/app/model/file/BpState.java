package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BpState {
    ALL("all"), RED("red"), GREEN("green"), AMBER("amber");

    private String state;

    BpState(String state) {
        this.state = state;
    }

    @JsonValue
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
