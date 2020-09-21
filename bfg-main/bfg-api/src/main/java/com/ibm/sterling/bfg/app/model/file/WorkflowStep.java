package com.ibm.sterling.bfg.app.model.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.time.LocalDateTime;

public class WorkflowStep {
    private Integer stepId;
    private String serviceName;
    private String exeState;
    private String advStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private LocalDateTime endTime;
    private String nodeExecuted;
    private String statusRpt;
    private String docId;
    private String wfcId;
    private Integer wfdId;
    private Integer wfdVersion;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean isInlineInvocation = false;

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getExeState() {
        return exeState;
    }

    public void setExeState(String exeState) {
        this.exeState = exeState;
    }

    public String getAdvStatus() {
        return advStatus;
    }

    public void setAdvStatus(String advStatus) {
        this.advStatus = advStatus;
    }

    @JsonFormat(pattern = "dd/MM/yyyy, HH:mm:ss")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "dd/MM/yyyy, HH:mm:ss")
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNodeExecuted() {
        return nodeExecuted;
    }

    public void setNodeExecuted(String nodeExecuted) {
        this.nodeExecuted = nodeExecuted;
    }

    public String getStatusRpt() {
        return statusRpt;
    }

    public void setStatusRpt(String statusRpt) {
        this.statusRpt = statusRpt;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getWfcId() {
        return wfcId;
    }

    public void setWfcId(String wfcId) {
        this.wfcId = wfcId;
    }

    public Integer getWfdId() {
        return wfdId;
    }

    public void setWfdId(Integer wfdId) {
        this.wfdId = wfdId;
    }

    public Integer getWfdVersion() {
        return wfdVersion;
    }

    public void setWfdVersion(Integer wfdVersion) {
        this.wfdVersion = wfdVersion;
    }

    public Boolean getInlineInvocation() {
        return isInlineInvocation;
    }

    public void setInlineInvocation(Boolean inlineInvocation) {
        isInlineInvocation = inlineInvocation;
    }

}
