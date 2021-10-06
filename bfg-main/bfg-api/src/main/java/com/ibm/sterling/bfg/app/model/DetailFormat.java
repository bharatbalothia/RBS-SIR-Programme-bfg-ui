package com.ibm.sterling.bfg.app.model;

public class DetailFormat {
    private String code;
    private String display;

    public DetailFormat() {
    }

    public DetailFormat(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

}
