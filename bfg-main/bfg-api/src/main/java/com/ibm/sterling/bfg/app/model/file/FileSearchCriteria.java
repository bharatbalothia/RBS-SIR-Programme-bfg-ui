package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.sterling.bfg.app.model.validation.file.DateValid;
import com.ibm.sterling.bfg.app.utils.TimeUtil;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class FileSearchCriteria extends SearchCriteria {
    private Integer id;
    private Long messageid;
    @JsonAlias("entityId")
    private Integer entityid;
    private Boolean override;
    private Boolean outbound;
    @JsonProperty("bp-state")
    @JsonAlias("bpstate")
    private String bpState;
    @JsonAlias("fileName")
    private String filename;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for from")
    private String from;
    @DateValid(pattern = "yyyy-MM-dd'T'HH:mm:ss", message = "Please match the requested format for to")
    private String to;

    public FileSearchCriteria() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMessageid() {
        return messageid;
    }

    public void setMessageid(Long messageid) {
        this.messageid = messageid;
    }

    public Integer getEntityid() {
        return entityid;
    }

    public void setEntityid(Integer entityid) {
        this.entityid = entityid;
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }

    public Boolean getOutbound() {
        return outbound;
    }

    public void setOutbound(Boolean outbound) {
        this.outbound = outbound;
    }

    public String getBpState() {
        return bpState;
    }

    public void setBpState(String bpState) {
        this.bpState = bpState;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @JsonIgnore
    @Override
    public boolean isDateValid() {
        return TimeUtil.isStringDateBefore(from, to);
    }

}
