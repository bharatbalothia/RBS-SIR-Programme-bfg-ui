package com.ibm.sterling.bfg.app.model.file;

public class TransactionDetails extends Transaction {
    private String entity;
    private String paymentBIC;
    private String filename;
    private String reference;
    private String fileID;
    private String service;
    private String statusLabel;

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

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

}
