package com.ibm.sterling.bfg.app.model.file;

public class TransactionDetails extends Transaction {
    private String entity;
    private String paymentBIC;
    private String filename;
    private String reference;
    private Boolean isoutbound;
    private String fileID;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getPaymentBIC() {
        return paymentBIC;
    }

    public void setPaymentBIC(String paymentBIC) {
        this.paymentBIC = paymentBIC;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getIsoutbound() {
        return isoutbound;
    }

    public void setIsoutbound(Boolean isoutbound) {
        this.isoutbound = isoutbound;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

}
