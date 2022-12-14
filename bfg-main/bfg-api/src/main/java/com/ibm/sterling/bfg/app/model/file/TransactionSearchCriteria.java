package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.sterling.bfg.app.model.validation.file.DateValid;
import com.ibm.sterling.bfg.app.utils.TimeUtil;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class TransactionSearchCriteria extends SearchCriteria {
    private String entity;

    @JsonAlias("paymentBIC")
    private String paymentbic;
    @JsonAlias("transactionID")
    private String transactionid;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for from")
    @JsonProperty("settlement-from")
    @JsonAlias("from")
    private String settlementFrom;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for to")
    @JsonProperty("settlement-to")
    @JsonAlias("to")
    private String settlementTo;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for timestamp from")
    @JsonProperty("ptimestamp-from")
    @JsonAlias("pFrom")
    private String ptimestampFrom;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for timestamp to")
    @JsonProperty("ptimestamp-to")
    @JsonAlias("pTo")
    private String ptimestampTo;
    @JsonProperty("has-message-id")
    @JsonAlias("hasMessageId")
    private Boolean hasMessageId;
    private Boolean payaway;
    @JsonProperty("transaction-type")
    @JsonAlias("transactionType")
    private TransactionType transactionType;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
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

    public Boolean getHasMessageId() {
        return hasMessageId;
    }

    public void setHasMessageId(Boolean hasMessageId) {
        this.hasMessageId = hasMessageId;
    }

    public Boolean getPayaway() {
        return payaway;
    }

    public void setPayaway(Boolean payaway) {
        this.payaway = payaway;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @JsonIgnore
    @Override
    public boolean isDateValid() {
        return TimeUtil.isStringDateBefore(settlementFrom, settlementTo) &&
        TimeUtil.isStringDateBefore(ptimestampFrom, ptimestampTo)  ;
    }
}
