package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.*;
import com.ibm.sterling.bfg.app.model.validation.file.FromToDateValid;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FromToDateValid
public class SearchCriteria {
    private String type;
    private List<String> direction;
    private String service;
    private String reference;
    private Integer wfid;
    private Integer status;
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer page = 0;
    @Min(1)
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer size = 10;
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer start = 0;
    @JsonIgnore
    private Integer totalRows;
    @JsonProperty("is-error")
    @JsonAlias("isError")
    private Boolean isError;
    @JsonProperty("exclude-archive")
    @JsonAlias("excludeArchive")
    private Boolean excludeArchive;

    public SearchCriteria() {
    }

    public SearchCriteria(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter
    public String getDirection() {
        return Optional.ofNullable(direction)
                .map(dir -> dir.stream().collect(Collectors.joining("&direction=")))
                .orElse(null);
    }

    public void setDirection(List<String> direction) {
        this.direction = direction;
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

    public Integer getWfid() {
        return wfid;
    }

    public void setWfid(Integer wfid) {
        this.wfid = wfid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    @JsonProperty(value = "rows", access = JsonProperty.Access.READ_ONLY)
    public Integer getSize() {
        return size;
    }

    @JsonProperty(value = "size", access = JsonProperty.Access.WRITE_ONLY)
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

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean error) {
        isError = error;
    }

    public Boolean getExcludeArchive() {
        return excludeArchive;
    }

    public void setExcludeArchive(Boolean excludeArchive) {
        this.excludeArchive = excludeArchive;
    }

    @JsonIgnore
    public boolean isDateValid() {
        return true;
    }
}
