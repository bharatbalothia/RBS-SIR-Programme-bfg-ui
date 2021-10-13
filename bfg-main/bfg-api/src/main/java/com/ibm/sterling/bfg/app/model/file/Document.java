package com.ibm.sterling.bfg.app.model.file;

public class Document {
    private Integer workflowId;
    private String documentName;
    private String storageType;
    private String documentId;
    private String documentPayload;

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentPayload() {
        return documentPayload;
    }

    public void setDocumentPayload(String documentPayload) {
        this.documentPayload = documentPayload;
    }

}
