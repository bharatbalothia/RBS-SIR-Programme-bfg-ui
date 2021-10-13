package com.ibm.sterling.bfg.app.model.validation.unique;

public enum EntityFieldName {

    ENTITY_SERVICE("entity"),
    ENTITY("entity"),
    SERVICE("service"),
    MQQUEUEOUT("mqQueueOut"),
    MAILBOXPATHOUT("mailboxPathOut");

    EntityFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String fieldName() {
        return fieldName;
    }

}
