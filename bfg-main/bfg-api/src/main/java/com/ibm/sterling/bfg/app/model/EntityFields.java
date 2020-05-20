package com.ibm.sterling.bfg.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class EntityFields {

    @Column(name = "REQUESTORDN")
    private String requestorDN;
    @Column(name = "RESPONDERDN")
    private String responderDN;
    @Column(name = "SERVICENAME")
    private String serviceName;
    @Column(name = "REQUESTTYPE")
    private String requestType;
    @Column(name = "SNF")
    private Boolean SnF = Boolean.FALSE;
    private Boolean trace = Boolean.FALSE;
    @Column(name = "DELIVERYNOTIF")
    private Boolean deliveryNotification = Boolean.FALSE;
    @Column(name = "DELIVERYNOTIFDN")
    private String deliveryNotifDN;
    @Column(name = "DELIVERYNOTIFRT")
    private String deliveryNotifRT;
    @Column(name = "REQUESTREF")
    private String requestRef;
    @Column(name = "FILEDESC")
    private String fileDesc;
    @Column(name = "FILEINFO")
    private String fileInfo;
    @Column(name = "TRANSFERDESC")
    private String transferDesc;
    @Column(name = "TRANSFERINFO")
    private String transferInfo;
    @Column(name = "COMPRESSION")
    private Boolean compression = Boolean.FALSE;
    @Column(name = "MAILBOXPATHIN")
    private String mailboxPathIn;
    @Column(name = "MAILBOXPATHOUT")
    private String mailboxPathOut;
    @Column(name = "MQQUEUEIN")
    private String mqQueueIn;
    @Column(name = "MQQUEUEOUT")
    private String mqQueueOut;
    @Column(name = "ENTITY_PARTICIPANT_TYPE")
    private String entityParticipantType;
    @Column(name = "DIRECT_PARTICIPANT")
    private String directParticipant;
    @Column(name = "MAXTRANSPERBULK")
    private Integer maxTransfersPerBulk;
    @Column(name = "MAXBULKSPERFILE")
    private Integer maxBulksPerFile;
    @Column(name = "STARTOFDAY")
    private Integer startOfDay;
    @Column(name = "ENDOFDAY")
    private Integer endOfDay;
    @Column(name = "CDNODE")
    private String cdNode;
    @Column(name = "IDF_WTOMSGID")
    private String idfWTOMsgId;
    @Column(name = "CDF_WTOMSGID")
    private String cdfWTOMsgId;
    @Column(name = "SDF_WTOMSGID")
    private String sdfWTOMsgId;
    @Column(name = "RSF_WTOMSGID")
    private String rsfWTOMsgId;
    @Column(name = "DNF_WTOMSGID")
    private String dnfWTOMsgId;
    @Column(name = "DVF_WTOMSGID")
    private String dvfWTOMsgId;
    @Column(name = "MSR_WTOMSGID")
    private String msrWTOMsgId;
    @Column(name = "PSR_WTOMSGID")
    private String psrWTOMsgId;
    @Column(name = "DRR_WTOMSGID")
    private String drrWTOMsgId;
    @Column(name = "RTF_WTOMSGID")
    private String rtfWTOMsgId;
    @Column(name = "MBP_WTOMSGID")
    private String mbpWTOMsgId;
    @Column(name = "MQ_HOST")
    private String mqHost;
    @Column(name = "MQ_PORT")
    private Integer mqPort;
    @Column(name = "MQ_QMANAGER")
    private String mqQManager;
    @Column(name = "MQ_CHANNEL")
    private String mqChannel;
    @Column(name = "MQ_QNAME")
    private String mqQueueName;
    @Column(name = "MQ_QBINDING")
    private String mqQueueBinding;
    @Column(name = "MQ_QCONTEXT")
    private String mqQueueContext;
    @Column(name = "MQ_DEBUG")
    private Integer mqDebug;
    @Column(name = "MQ_SSLOPTION")
    private String mqSSLoptions;
    @Column(name = "MQ_SSLCIPHERS")
    private String mqSSLciphers;
    @Column(name = "MQ_SSLSYSTEMCERTID")
    private String mqSSLkey;
    @Column(name = "MQ_SSLCACERTID")
    private String mqSSLcaCert;
    @Column(name = "MQ_HEADER")
    private String mqHeader;
    @Column(name = "MQ_SESSIONTIMEOUT")
    private Integer mqSessionTimeout;

    public String getRequestorDN() {
        return requestorDN;
    }

    public void setRequestorDN(String requestorDN) {
        this.requestorDN = requestorDN;
    }

    public String getResponderDN() {
        return responderDN;
    }

    public void setResponderDN(String responderDN) {
        this.responderDN = responderDN;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Boolean getSnF() {
        return SnF;
    }

    public void setSnF(Boolean snF) {
        SnF = snF;
    }

    public Boolean getTrace() {
        return trace;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public Boolean getDeliveryNotification() {
        return deliveryNotification;
    }

    public void setDeliveryNotification(Boolean deliveryNotification) {
        this.deliveryNotification = deliveryNotification;
    }

    public String getDeliveryNotifDN() {
        return deliveryNotifDN;
    }

    public void setDeliveryNotifDN(String deliveryNotifDN) {
        this.deliveryNotifDN = deliveryNotifDN;
    }

    public String getDeliveryNotifRT() {
        return deliveryNotifRT;
    }

    public void setDeliveryNotifRT(String deliveryNotifRT) {
        this.deliveryNotifRT = deliveryNotifRT;
    }

    public String getRequestRef() {
        return requestRef;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getTransferDesc() {
        return transferDesc;
    }

    public void setTransferDesc(String transferDesc) {
        this.transferDesc = transferDesc;
    }

    public String getTransferInfo() {
        return transferInfo;
    }

    public void setTransferInfo(String transferInfo) {
        this.transferInfo = transferInfo;
    }

    public Boolean getCompression() {
        return compression;
    }

    public void setCompression(Boolean compression) {
        this.compression = compression;
    }

    public String getMailboxPathIn() {
        return mailboxPathIn;
    }

    public void setMailboxPathIn(String mailboxPathIn) {
        this.mailboxPathIn = mailboxPathIn;
    }

    public String getMailboxPathOut() {
        return mailboxPathOut;
    }

    public void setMailboxPathOut(String mailboxPathOut) {
        this.mailboxPathOut = mailboxPathOut;
    }

    public String getMqQueueIn() {
        return mqQueueIn;
    }

    public void setMqQueueIn(String mqQueueIn) {
        this.mqQueueIn = mqQueueIn;
    }

    public String getMqQueueOut() {
        return mqQueueOut;
    }

    public void setMqQueueOut(String mqQueueOut) {
        this.mqQueueOut = mqQueueOut;
    }

    public String getEntityParticipantType() {
        return entityParticipantType;
    }

    public void setEntityParticipantType(String entityParticipantType) {
        this.entityParticipantType = entityParticipantType;
    }

    public String getDirectParticipant() {
        return directParticipant;
    }

    public void setDirectParticipant(String directParticipant) {
        this.directParticipant = directParticipant;
    }

    public Integer getMaxTransfersPerBulk() {
        return maxTransfersPerBulk;
    }

    public void setMaxTransfersPerBulk(Integer maxTransfersPerBulk) {
        this.maxTransfersPerBulk = maxTransfersPerBulk;
    }

    public Integer getMaxBulksPerFile() {
        return maxBulksPerFile;
    }

    public void setMaxBulksPerFile(Integer maxBulksPerFile) {
        this.maxBulksPerFile = maxBulksPerFile;
    }

    public Integer getStartOfDay() {
        return startOfDay;
    }

    public void setStartOfDay(Integer startOfDay) {
        this.startOfDay = startOfDay;
    }

    public Integer getEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(Integer endOfDay) {
        this.endOfDay = endOfDay;
    }

    public String getCdNode() {
        return cdNode;
    }

    public void setCdNode(String cdNode) {
        this.cdNode = cdNode;
    }

    public String getIdfWTOMsgId() {
        return idfWTOMsgId;
    }

    public void setIdfWTOMsgId(String idfWTOMsgId) {
        this.idfWTOMsgId = idfWTOMsgId;
    }

    public String getCdfWTOMsgId() {
        return cdfWTOMsgId;
    }

    public void setCdfWTOMsgId(String cdfWTOMsgId) {
        this.cdfWTOMsgId = cdfWTOMsgId;
    }

    public String getSdfWTOMsgId() {
        return sdfWTOMsgId;
    }

    public void setSdfWTOMsgId(String sdfWTOMsgId) {
        this.sdfWTOMsgId = sdfWTOMsgId;
    }

    public String getRsfWTOMsgId() {
        return rsfWTOMsgId;
    }

    public void setRsfWTOMsgId(String rsfWTOMsgId) {
        this.rsfWTOMsgId = rsfWTOMsgId;
    }

    public String getDnfWTOMsgId() {
        return dnfWTOMsgId;
    }

    public void setDnfWTOMsgId(String dnfWTOMsgId) {
        this.dnfWTOMsgId = dnfWTOMsgId;
    }

    public String getDvfWTOMsgId() {
        return dvfWTOMsgId;
    }

    public void setDvfWTOMsgId(String dvfWTOMsgId) {
        this.dvfWTOMsgId = dvfWTOMsgId;
    }

    public String getMsrWTOMsgId() {
        return msrWTOMsgId;
    }

    public void setMsrWTOMsgId(String msrWTOMsgId) {
        this.msrWTOMsgId = msrWTOMsgId;
    }

    public String getPsrWTOMsgId() {
        return psrWTOMsgId;
    }

    public void setPsrWTOMsgId(String psrWTOMsgId) {
        this.psrWTOMsgId = psrWTOMsgId;
    }

    public String getDrrWTOMsgId() {
        return drrWTOMsgId;
    }

    public void setDrrWTOMsgId(String drrWTOMsgId) {
        this.drrWTOMsgId = drrWTOMsgId;
    }

    public String getRtfWTOMsgId() {
        return rtfWTOMsgId;
    }

    public void setRtfWTOMsgId(String rtfWTOMsgId) {
        this.rtfWTOMsgId = rtfWTOMsgId;
    }

    public String getMbpWTOMsgId() {
        return mbpWTOMsgId;
    }

    public void setMbpWTOMsgId(String mbpWTOMsgId) {
        this.mbpWTOMsgId = mbpWTOMsgId;
    }

    public String getMqHost() {
        return mqHost;
    }

    public void setMqHost(String mqHost) {
        this.mqHost = mqHost;
    }

    public Integer getMqPort() {
        return mqPort;
    }

    public void setMqPort(Integer mqPort) {
        this.mqPort = mqPort;
    }

    public String getMqQManager() {
        return mqQManager;
    }

    public void setMqQManager(String mqQManager) {
        this.mqQManager = mqQManager;
    }

    public String getMqChannel() {
        return mqChannel;
    }

    public void setMqChannel(String mqChannel) {
        this.mqChannel = mqChannel;
    }

    public String getMqQueueName() {
        return mqQueueName;
    }

    public void setMqQueueName(String mqQueueName) {
        this.mqQueueName = mqQueueName;
    }

    public String getMqQueueBinding() {
        return mqQueueBinding;
    }

    public void setMqQueueBinding(String mqQueueBinding) {
        this.mqQueueBinding = mqQueueBinding;
    }

    public String getMqQueueContext() {
        return mqQueueContext;
    }

    public void setMqQueueContext(String mqQueueContext) {
        this.mqQueueContext = mqQueueContext;
    }

    public Integer getMqDebug() {
        return mqDebug;
    }

    public void setMqDebug(Integer mqDebug) {
        this.mqDebug = mqDebug;
    }

    public String getMqSSLoptions() {
        return mqSSLoptions;
    }

    public void setMqSSLoptions(String mqSSLoptions) {
        this.mqSSLoptions = mqSSLoptions;
    }

    public String getMqSSLciphers() {
        return mqSSLciphers;
    }

    public void setMqSSLciphers(String mqSSLciphers) {
        this.mqSSLciphers = mqSSLciphers;
    }

    public String getMqSSLkey() {
        return mqSSLkey;
    }

    public void setMqSSLkey(String mqSSLkey) {
        this.mqSSLkey = mqSSLkey;
    }

    public String getMqSSLcaCert() {
        return mqSSLcaCert;
    }

    public void setMqSSLcaCert(String mqSSLcaCert) {
        this.mqSSLcaCert = mqSSLcaCert;
    }

    public String getMqHeader() {
        return mqHeader;
    }

    public void setMqHeader(String mqHeader) {
        this.mqHeader = mqHeader;
    }

    public Integer getMqSessionTimeout() {
        return mqSessionTimeout;
    }

    public void setMqSessionTimeout(Integer mqSessionTimeout) {
        this.mqSessionTimeout = mqSessionTimeout;
    }
}
