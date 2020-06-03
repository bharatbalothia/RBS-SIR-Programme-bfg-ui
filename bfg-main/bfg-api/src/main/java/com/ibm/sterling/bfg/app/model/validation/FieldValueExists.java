package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.Entity;

public interface FieldValueExists {
    boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
    boolean fieldValueExistsPut(Entity entity, String fieldName) throws UnsupportedOperationException;
}