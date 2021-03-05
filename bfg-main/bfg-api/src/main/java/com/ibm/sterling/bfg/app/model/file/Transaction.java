package com.ibm.sterling.bfg.app.model.file;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

public class Transaction {
    private Integer id;
    private Integer status;
    private String transactionID;
    private String type;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime settleDate;
    private Double settleAmount;
    private Integer workflowID;
    private Direction direction;
    private String fileID;
    private String docID;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timestamp;
    private Boolean payaway;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public LocalDateTime getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(LocalDateTime settleDate) {
        this.settleDate = settleDate;
    }

    public Double getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(Double settleAmount) {
        this.settleAmount = settleAmount;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDirection() {
        return Optional.ofNullable(direction).map(Direction::direction).orElse("");
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Boolean getPayaway() {
        return payaway;
    }

    public void setPayaway(Boolean payaway) {
        this.payaway = payaway;
    }
}
