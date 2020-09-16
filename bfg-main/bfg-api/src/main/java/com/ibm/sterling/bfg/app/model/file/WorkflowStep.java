package com.ibm.sterling.bfg.app.model.file;

public class WorkflowStep {
    private String serviceName;
    private String exeState;
    private String advStatus;
    private String startTime;
    private String endTime;
    private String nodeExecuted;
    private String statusRpt;
    private String docId;
    private String wfcId;
    private Integer wfdId;
    private Integer wfdVersion;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

}
