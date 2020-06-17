package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

public enum ScheduleFieldName {

    ISWINDOW("isWindow"),
    TIMESTART("timeStart"),
    WINDOWEND("windowEnd"),
    WINDOWINTERVAL("windowInterval");

    ScheduleFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String fieldName() {
        return fieldName;
    }

}
