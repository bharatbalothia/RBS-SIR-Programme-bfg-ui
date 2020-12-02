package com.ibm.sterling.bfg.app.model.changecontrol;

public enum Operation {
    CREATE("CREATE"),
    UPDATE("EDIT"),
    DELETE("DELETE");

    private final String OPERATION_PERM;

    Operation(String operationPerm) {
        OPERATION_PERM = operationPerm;
    }

    public String getOperationPerm() { return OPERATION_PERM; }
}
