package com.ibm.sterling.bfg.app.model.file;

import com.ibm.sterling.bfg.app.model.DetailFormat;

import java.util.Optional;

public class BPDetails {
    private String name;
    private String startMode;
    private DetailFormat documentTracking;
    private DetailFormat queue;
    private DetailFormat recoveryLevel;
    private DetailFormat softstopRecoveryLevel;
    private String lifespanDays = "Default";
    private String lifespanHours = "Default";
    private String lifespanType = "System Level";
    private DetailFormat removalMethod;
    private DetailFormat eventReportingLevel;
    private String wfdVersion;
    private DetailFormat onfaultProcessing;
    private DetailFormat enableTransaction;
    private DetailFormat persistenceLevel;
    private DetailFormat documentStorage;
    private String expedite;
    private String deadlineHours;
    private String deadlineMinutes;
    private String firstNotificationHours;
    private String firstNotificationMinutes;
    private String secondNotificationHours;
    private String secondNotificationMinutes;
    private String description;
    private String node;
    private DetailFormat nodePreference;
    private String businessProcess;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentTracking() {
        return Optional.ofNullable(documentTracking).map(DetailFormat::getCode).orElse("");
    }

    public void setDocumentTracking(DetailFormat documentTracking) {
        this.documentTracking = documentTracking;
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public String getQueue() {
        return Optional.ofNullable(queue).map(DetailFormat::getDisplay).orElse("");
    }

    public void setQueue(DetailFormat queue) {
        this.queue = queue;
    }

    public String getRecoveryLevel() {
        return Optional.ofNullable(recoveryLevel).map(DetailFormat::getDisplay).orElse("");
    }

    public void setRecoveryLevel(DetailFormat recoveryLevel) {
        this.recoveryLevel = recoveryLevel;
    }

    public String getSoftstopRecoveryLevel() {
        return Optional.ofNullable(softstopRecoveryLevel).map(DetailFormat::getDisplay).orElse("");
    }

    public void setSoftstopRecoveryLevel(DetailFormat softstopRecoveryLevel) {
        this.softstopRecoveryLevel = softstopRecoveryLevel;
    }

    public String getLifespanDays() {
        return lifespanDays;
    }

    public void setLifespanDays(String lifespanDays) {
        this.lifespanDays = lifespanDays;
    }

    public String getLifespanHours() {
        return lifespanHours;
    }

    public void setLifespanHours(String lifespanHours) {
        this.lifespanHours = lifespanHours;
    }

    public String getLifespanType() {
        return lifespanType;
    }

    public void setLifespanType(String lifespanType) {
        this.lifespanType = lifespanType;
    }

    public String getRemovalMethod() {
        return Optional.ofNullable(removalMethod).map(DetailFormat::getDisplay).orElse("");
    }

    public void setRemovalMethod(DetailFormat removalMethod) {
        this.removalMethod = removalMethod;
    }

    public String getEventReportingLevel() {
        return Optional.ofNullable(eventReportingLevel).map(DetailFormat::getDisplay).orElse("");
    }

    public void setEventReportingLevel(DetailFormat eventReportingLevel) {
        this.eventReportingLevel = eventReportingLevel;
    }

    public String getWfdVersion() {
        return wfdVersion;
    }

    public void setWfdVersion(String wfdVersion) {
        this.wfdVersion = wfdVersion;
    }

    public String getOnfaultProcessing() {
        return Optional.ofNullable(onfaultProcessing).map(DetailFormat::getCode).orElse("");
    }

    public void setOnfaultProcessing(DetailFormat onfaultProcessing) {
        this.onfaultProcessing = onfaultProcessing;
    }

    public String getEnableTransaction() {
        return Optional.ofNullable(enableTransaction).map(DetailFormat::getCode).orElse("");
    }

    public void setEnableTransaction(DetailFormat enableTransaction) {
        this.enableTransaction = enableTransaction;
    }

    public String getPersistenceLevel() {
        return Optional.ofNullable(persistenceLevel).map(DetailFormat::getDisplay).orElse("");
    }

    public void setPersistenceLevel(DetailFormat persistenceLevel) {
        this.persistenceLevel = persistenceLevel;
    }

    public String getDocumentStorage() {
        return Optional.ofNullable(documentStorage).map(DetailFormat::getDisplay).orElse("");
    }

    public void setDocumentStorage(DetailFormat documentStorage) {
        this.documentStorage = documentStorage;
    }

    public String getExpedite() {
        return expedite;
    }

    public void setExpedite(String expedite) {
        this.expedite = expedite;
    }

    public String getDeadlineHours() {
        return deadlineHours;
    }

    public void setDeadlineHours(String deadlineHours) {
        this.deadlineHours = deadlineHours;
    }

    public String getDeadlineMinutes() {
        return deadlineMinutes;
    }

    public void setDeadlineMinutes(String deadlineMinutes) {
        this.deadlineMinutes = deadlineMinutes;
    }

    public String getFirstNotificationHours() {
        return firstNotificationHours;
    }

    public void setFirstNotificationHours(String firstNotificationHours) {
        this.firstNotificationHours = firstNotificationHours;
    }

    public String getFirstNotificationMinutes() {
        return firstNotificationMinutes;
    }

    public void setFirstNotificationMinutes(String firstNotificationMinutes) {
        this.firstNotificationMinutes = firstNotificationMinutes;
    }

    public String getSecondNotificationHours() {
        return secondNotificationHours;
    }

    public void setSecondNotificationHours(String secondNotificationHours) {
        this.secondNotificationHours = secondNotificationHours;
    }

    public String getSecondNotificationMinutes() {
        return secondNotificationMinutes;
    }

    public void setSecondNotificationMinutes(String secondNotificationMinutes) {
        this.secondNotificationMinutes = secondNotificationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setNodePreference(DetailFormat nodePreference) {
        this.nodePreference = nodePreference;
    }

    public String getNodePreference() {
        return Optional.ofNullable(nodePreference).map(DetailFormat::getDisplay).orElse("");
    }

}
