package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.Min;

public class SearchCriteria {
    private String type;
    private String service;
    private String reference;
    private Boolean outbound;
    private Integer wfid;
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer page = 0;
    @JsonAlias("size")
    @JsonSetter(nulls = Nulls.SKIP)
    @Min(1)
    @JsonProperty("rows")
    private Integer size = 10;
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer start = 0;
    @JsonIgnore
    private Integer totalRows;

    public SearchCriteria() {
    }

    public SearchCriteria(Integer page, @Min(1) Integer size) {
        this.page = page;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }
}
