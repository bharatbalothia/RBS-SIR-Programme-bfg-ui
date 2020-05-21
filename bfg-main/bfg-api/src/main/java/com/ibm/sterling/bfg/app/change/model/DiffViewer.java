package com.ibm.sterling.bfg.app.change.model;

public class DiffViewer {
    private String field;
    private String beforeValue;
    private String afterValue;
    private boolean isDifferent;

    public DiffViewer(String field, String before, String after){
        setField(field);
        setBeforeValue(before);
        setAfterValue(after);
        compare();
    }

    public DiffViewer(String field, int before, int after){
        setField(field);
        setBeforeValue(String.valueOf(before));
        setAfterValue(String.valueOf(after));
        compare();
    }
    public DiffViewer(String field, Integer before, Integer after){
        setField(field);
        setBeforeValue(String.valueOf(before));
        setAfterValue(String.valueOf(after));
        compare();
    }
    public DiffViewer(String field, boolean before, boolean after){
        setField(field);
        setBeforeValue(String.valueOf(before));
        setAfterValue(String.valueOf(after));
        compare();
    }
    public DiffViewer(String field, Boolean before, Boolean after){
        setField(field);
        setBeforeValue(String.valueOf(before));
        setAfterValue(String.valueOf(after));
        compare();
    }

    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isDifferent(){
        return isDifferent;
    }

    public void compare(){
        if((getBeforeValue() == null || getBeforeValue().equalsIgnoreCase("")) &&
                (getAfterValue() == null || getAfterValue().equalsIgnoreCase(""))) {
            isDifferent = false;
        } else {
            isDifferent = !getBeforeValue().equals(getAfterValue());
        }
    }
}
