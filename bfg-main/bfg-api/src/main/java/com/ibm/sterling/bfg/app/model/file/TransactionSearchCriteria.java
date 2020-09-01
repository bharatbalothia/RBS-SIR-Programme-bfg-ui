package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.*;
import com.ibm.sterling.bfg.app.model.validation.DateValid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class TransactionSearchCriteria implements SearchCriteria{
    private String type;
    private String entity;
    private String service;
    private String reference;
    private String direction;
    private Boolean outbound;
    private Integer wfid;
    private String paymentbic;
    private String transactionid;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for from")
    @JsonProperty("settlement-from")
    @JsonAlias("settlementFrom")
    private String settlementFrom;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for to")
    @JsonProperty("settlement-to")
    @JsonAlias("settlementTo")
    private String settlementTo;
    @JsonAlias("page")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer start = 0;
    @JsonAlias("size")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer rows = 10;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Boolean getOutbound() {
        return outbound;
    }

    public void setOutbound(Boolean outbound) {
        this.outbound = outbound;
    }

    public Integer getWfid() {
        return wfid;
    }

    public void setWfid(Integer wfid) {
        this.wfid = wfid;
    }

    public String getPaymentbic() {
        return paymentbic;
    }

    public void setPaymentbic(String paymentbic) {
        this.paymentbic = paymentbic;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getSettlementFrom() {
        return settlementFrom;
    }

    public void setSettlementFrom(String settlementFrom) {
        this.settlementFrom = settlementFrom;
    }

    public String getSettlementTo() {
        return settlementTo;
    }

    public void setSettlementTo(String settlementTo) {
        this.settlementTo = settlementTo;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
