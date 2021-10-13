package com.ibm.sterling.bfg.app.model.audit;

public enum ActionType {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    ActionType(String attributeName) {
        this.attributeName = attributeName;
    }

    private String attributeName;

    public String attributeName() {
        return attributeName;
    }

}
