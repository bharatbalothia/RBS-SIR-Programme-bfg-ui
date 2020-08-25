package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

public class Entity {
    private Integer entityId;
    private String entity;
    @JsonInclude(Include.NON_NULL)
    private String error;

    public Entity() {
    }

    public Entity(Integer entityId, String entity) {
        this.entityId = entityId;
        this.entity = entity;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
