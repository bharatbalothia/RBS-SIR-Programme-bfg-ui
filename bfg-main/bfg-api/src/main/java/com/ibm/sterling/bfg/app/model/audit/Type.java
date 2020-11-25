package com.ibm.sterling.bfg.app.model.audit;

public enum Type {
    ENTITY("entity"),
    TRUSTED_CERTIFICATE("trusted certificate");

    Type(String attributeName) {
        this.attributeName = attributeName;
    }

    private String attributeName;

    public String attributeName() {
        return attributeName;
    }

}
