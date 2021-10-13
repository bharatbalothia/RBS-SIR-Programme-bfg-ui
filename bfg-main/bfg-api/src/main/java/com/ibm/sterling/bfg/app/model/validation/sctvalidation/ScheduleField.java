package com.ibm.sterling.bfg.app.model.validation.sctvalidation;

import com.ibm.sterling.bfg.app.model.validation.Field;

public enum ScheduleField implements Field {
    ISWINDOW("isWindow"),
    TIMESTART("timeStart"),
    WINDOWEND("windowEnd"),
    WINDOWINTERVAL("windowInterval");

    ScheduleField(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String fieldName() {
        return fieldName;
    }

}
