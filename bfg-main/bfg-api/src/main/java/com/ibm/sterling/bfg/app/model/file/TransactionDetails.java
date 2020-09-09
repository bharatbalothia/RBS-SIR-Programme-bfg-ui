package com.ibm.sterling.bfg.app.model.file;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TransactionDetails extends Transaction {
    private String entity;
    private String paymentBIC;
    private String filename;
    private String reference;
    private Boolean isoutbound;
    private String fileID;
    private String service;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timestamp;

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
