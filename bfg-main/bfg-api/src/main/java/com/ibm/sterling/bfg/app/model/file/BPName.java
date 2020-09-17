package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BPName {
    @JsonProperty("_id")
    private String name;
    private Integer wfdVersion;
    private Integer wfdID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWfdVersion() {
        return wfdVersion;
    }

    public void setWfdVersion(Integer wfdVersion) {
        this.wfdVersion = wfdVersion;
    }

    public Integer getWfdID() {
        return wfdID;
    }

    public void setWfdID(Integer wfdID) {
        this.wfdID = wfdID;
    }
}
