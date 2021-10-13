package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Optional;

public class SEPAFile {
    private Integer id;
    private String filename;
    private String type;
    private String docID;
    private Integer transactionTotal;
    private Direction direction;
    private Double settleAmountTotal = 0.0;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private Integer messageID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public Integer getTransactionTotal() {
        return transactionTotal;
    }

    public void setTransactionTotal(Integer transactionTotal) {
        this.transactionTotal = transactionTotal;
    }

    public String getDirection() {
        return Optional.ofNullable(direction).map(Direction::direction).orElse("");
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Double getSettleAmountTotal() {
        return settleAmountTotal;
    }

    public void setSettleAmountTotal(Double settleAmountTotal) {
        this.settleAmountTotal = settleAmountTotal;
    }

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }
}
