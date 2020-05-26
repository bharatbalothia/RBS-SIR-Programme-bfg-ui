package com.ibm.sterling.bfg.app.model.validation;

public interface FieldValueExists {
    boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
}