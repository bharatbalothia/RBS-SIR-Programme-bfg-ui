package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {
    @JsonProperty("transaction")
    TRANSACTION,
    @JsonProperty("payaway")
    PAYAWAY;
}
