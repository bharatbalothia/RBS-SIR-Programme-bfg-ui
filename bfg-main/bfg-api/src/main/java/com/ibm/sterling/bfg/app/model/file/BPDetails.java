package com.ibm.sterling.bfg.app.model.file;

import java.util.Optional;

public class BPDetails {
    private String name;
    private String startMode;
    private BPDetailFormat documentTracking;
    private BPDetailFormat queue;
    private BPDetailFormat recoveryLevel;
    private BPDetailFormat softstopRecoveryLevel;
    private String lifespanDays = "Default";
    private String lifespanHours = "Default";
    private String lifespanType = "System Level";
    private BPDetailFormat removalMethod;
    private BPDetailFormat eventReportingLevel;
    private String wfdVersion;
    private BPDetailFormat onfaultProcessing;
    private BPDetailFormat enableTransaction;
    private BPDetailFormat persistenceLevel;
    private BPDetailFormat documentStorage;
    private String expedite;
    private String deadlineHours;
    private String deadlineMinutes;
    private String firstNotificationHours;
    private String firstNotificationMinutes;
    private String secondNotificationHours;
    private String secondNotificationMinutes;
    private String description;
    private String node;
    private BPDetailFormat nodePreference;
    private String businessProcess;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentTracking() {
        return Optional.ofNullable(documentTracking).map(BPDetailFormat::getCode).orElse("");
    }

    public void setDocumentTracking(BPDetailFormat documentTracking) {
        this.documentTracking = documentTracking;
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public String getQueue() {
        return Optional.ofNullable(queue).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setQueue(BPDetailFormat queue) {
        this.queue = queue;
    }

    public String getRecoveryLevel() {
        return Optional.ofNullable(recoveryLevel).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setRecoveryLevel(BPDetailFormat recoveryLevel) {
        this.recoveryLevel = recoveryLevel;
    }

    public String getSoftstopRecoveryLevel() {
        return Optional.ofNullable(softstopRecoveryLevel).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setSoftstopRecoveryLevel(BPDetailFormat softstopRecoveryLevel) {
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
        return Optional.ofNullable(removalMethod).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setRemovalMethod(BPDetailFormat removalMethod) {
        this.removalMethod = removalMethod;
    }

    public String getEventReportingLevel() {
        return Optional.ofNullable(eventReportingLevel).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setEventReportingLevel(BPDetailFormat eventReportingLevel) {
        this.eventReportingLevel = eventReportingLevel;
    }

    public String getWfdVersion() {
        return wfdVersion;
    }

    public void setWfdVersion(String wfdVersion) {
        this.wfdVersion = wfdVersion;
    }

    public String getOnfaultProcessing() {
        return Optional.ofNullable(onfaultProcessing).map(BPDetailFormat::getCode).orElse("");
    }

    public void setOnfaultProcessing(BPDetailFormat onfaultProcessing) {
        this.onfaultProcessing = onfaultProcessing;
    }

    public String getEnableTransaction() {
        return Optional.ofNullable(enableTransaction).map(BPDetailFormat::getCode).orElse("");
    }

    public void setEnableTransaction(BPDetailFormat enableTransaction) {
        this.enableTransaction = enableTransaction;
    }

    public String getPersistenceLevel() {
        return Optional.ofNullable(persistenceLevel).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setPersistenceLevel(BPDetailFormat persistenceLevel) {
        this.persistenceLevel = persistenceLevel;
    }

    public String getDocumentStorage() {
        return Optional.ofNullable(documentStorage).map(BPDetailFormat::getDisplay).orElse("");
    }

    public void setDocumentStorage(BPDetailFormat documentStorage) {
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

    public void setNodePreference(BPDetailFormat nodePreference) {
        this.nodePreference = nodePreference;
    }

    public String getNodePreference() {
        return Optional.ofNullable(nodePreference).map(BPDetailFormat::getDisplay).orElse("");
    }
}
