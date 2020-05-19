package com.ibm.sterling.bfg.app.model;

import org.apache.logging.log4j.*;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

@javax.persistence.Entity
@Table(name = "SCT_ENTITY")
public class Entity implements Serializable, ByteEntity {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(Entity.class);

    @Id
    @Column(name = "ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_ENTITY_IDSEQ")
    @SequenceGenerator(sequenceName = "SCT_ENTITY_IDSEQ", name = "SCT_ENTITY_IDSEQ", allocationSize = 1)
    private Integer entityId;
    private String entity;
    private String service;
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
    @Transient
    private List schedules = new ArrayList();
    @Transient
    private List deletedSchedules = new ArrayList();
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
    @Transient
    private Boolean routeInbound = Boolean.TRUE;
    @Transient
    private Boolean routeOutbound = Boolean.TRUE;
    @Transient
    private Boolean inboundDir = Boolean.FALSE;
    @Transient
    private Boolean inboundRoutingRule = Boolean.FALSE;

    @Column(name = "ROUTE_REQUESTORDN")
    private String inboundRequestorDN = "";
    @Column(name = "ROUTE_RESPONDERDN")
    private String inboundResponderDN = "";
    @Column(name = "ROUTE_SERVICE")
    private String inboundService = "";
    @Column(name = "ROUTE_REQUESTTYPE")
    private String inboundType = "";
    @Transient
    private String[] inboundRequestType = new String[0];
    @Column(name = "NONREPUDIATION")
    private Boolean nonRepudiation = Boolean.FALSE;
    @Column(name = "PAUSE_INBOUND")
    private Boolean pauseInbound = Boolean.FALSE;
    @Column(name = "PAUSE_OUTBOUND")
    private Boolean pauseOutbound = Boolean.FALSE;
    @Column(name = "ISDELETED")
    private Boolean deleted = Boolean.FALSE;
    @Transient
    private String changeID = "";
    @Transient
    private String changerComments = "";
    @Transient
    private Boolean irishStep2 = Boolean.FALSE;

    @Column(name = "E2ESIGNING")
    private String e2eSigning;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

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

    public List getSchedules() {
        return schedules;
    }

    public void setSchedules(List schedules) {
        this.schedules = schedules;
    }

    public List getDeletedSchedules() {
        return deletedSchedules;
    }

    public void setDeletedSchedules(List deletedSchedules) {
        this.deletedSchedules = deletedSchedules;
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

    public Boolean getRouteInbound() {
        return routeInbound;
    }

    public void setRouteInbound(Boolean routeInbound) {
        this.routeInbound = routeInbound;
    }

    public Boolean getRouteOutbound() {
        return routeOutbound;
    }

    public void setRouteOutbound(Boolean routeOutbound) {
        this.routeOutbound = routeOutbound;
    }

    public Boolean getInboundDir() {
        return inboundDir;
    }

    public void setInboundDir(Boolean inboundDir) {
        this.inboundDir = inboundDir;
    }

    public Boolean getInboundRoutingRule() {
        return inboundRoutingRule;
    }

    public void setInboundRoutingRule(Boolean inboundRoutingRule) {
        this.inboundRoutingRule = inboundRoutingRule;
    }

    public String getInboundRequestorDN() {
        return inboundRequestorDN;
    }

    public void setInboundRequestorDN(String inboundRequestorDN) {
        this.inboundRequestorDN = inboundRequestorDN;
    }

    public String getInboundResponderDN() {
        return inboundResponderDN;
    }

    public void setInboundResponderDN(String inboundResponderDN) {
        this.inboundResponderDN = inboundResponderDN;
    }

    public String getInboundService() {
        return inboundService;
    }

    public void setInboundService(String inboundService) {
        this.inboundService = inboundService;
    }

    public String getInboundType() {
        return inboundType;
    }

    public void setInboundType(String inboundType) {
        this.inboundType = inboundType;
    }

    public String[] getInboundRequestType() {
        return inboundRequestType;
    }

    public void setInboundRequestType(String[] inboundRequestType) {
        this.inboundRequestType = inboundRequestType;
    }

    public Boolean getNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(Boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public Boolean getPauseInbound() {
        return pauseInbound;
    }

    public void setPauseInbound(Boolean pauseInbound) {
        this.pauseInbound = pauseInbound;
    }

    public Boolean getPauseOutbound() {
        return pauseOutbound;
    }

    public void setPauseOutbound(Boolean pauseOutbound) {
        this.pauseOutbound = pauseOutbound;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getChangeID() {
        return changeID;
    }

    public void setChangeID(String changeID) {
        this.changeID = changeID;
    }

    public String getChangerComments() {
        return changerComments;
    }

    public void setChangerComments(String changerComments) {
        this.changerComments = changerComments;
    }

    public Boolean getIrishStep2() {
        return irishStep2;
    }

    public void setIrishStep2(Boolean irishStep2) {
        this.irishStep2 = irishStep2;
    }

    public String getE2eSigning() {
        return e2eSigning;
    }

    public void setE2eSigning(String e2eSigning) {
        this.e2eSigning = e2eSigning;
    }

//    public byte[] getObjectBytes() {
//        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oout = new ObjectOutputStream(baos)) {
//            oout.writeObject(this);
//            return baos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Override
    public String toString() {
        return "Entity{" +
                "entityId=" + entityId +
                ", entity='" + entity + '\'' +
                ", service='" + service + '\'' +
                ", requestorDN='" + requestorDN + '\'' +
                ", responderDN='" + responderDN + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", requestType='" + requestType + '\'' +
                ", SnF=" + SnF +
                ", trace=" + trace +
                ", deliveryNotification=" + deliveryNotification +
                ", deliveryNotifDN='" + deliveryNotifDN + '\'' +
                ", deliveryNotifRT='" + deliveryNotifRT + '\'' +
                ", requestRef='" + requestRef + '\'' +
                ", fileDesc='" + fileDesc + '\'' +
                ", fileInfo='" + fileInfo + '\'' +
                ", transferDesc='" + transferDesc + '\'' +
                ", transferInfo='" + transferInfo + '\'' +
                ", compression=" + compression +
                ", mailboxPathIn='" + mailboxPathIn + '\'' +
                ", mailboxPathOut='" + mailboxPathOut + '\'' +
                ", mqQueueIn='" + mqQueueIn + '\'' +
                ", mqQueueOut='" + mqQueueOut + '\'' +
                ", entityParticipantType='" + entityParticipantType + '\'' +
                ", directParticipant='" + directParticipant + '\'' +
                ", maxTransfersPerBulk=" + maxTransfersPerBulk +
                ", maxBulksPerFile=" + maxBulksPerFile +
                ", startOfDay=" + startOfDay +
                ", endOfDay=" + endOfDay +
                ", schedules=" + schedules +
                ", deletedSchedules=" + deletedSchedules +
                ", cdNode='" + cdNode + '\'' +
                ", idfWTOMsgId='" + idfWTOMsgId + '\'' +
                ", cdfWTOMsgId='" + cdfWTOMsgId + '\'' +
                ", sdfWTOMsgId='" + sdfWTOMsgId + '\'' +
                ", rsfWTOMsgId='" + rsfWTOMsgId + '\'' +
                ", dnfWTOMsgId='" + dnfWTOMsgId + '\'' +
                ", dvfWTOMsgId='" + dvfWTOMsgId + '\'' +
                ", msrWTOMsgId='" + msrWTOMsgId + '\'' +
                ", psrWTOMsgId='" + psrWTOMsgId + '\'' +
                ", drrWTOMsgId='" + drrWTOMsgId + '\'' +
                ", rtfWTOMsgId='" + rtfWTOMsgId + '\'' +
                ", mbpWTOMsgId='" + mbpWTOMsgId + '\'' +
                ", mqHost='" + mqHost + '\'' +
                ", mqPort=" + mqPort +
                ", mqQManager='" + mqQManager + '\'' +
                ", mqChannel='" + mqChannel + '\'' +
                ", mqQueueName='" + mqQueueName + '\'' +
                ", mqQueueBinding='" + mqQueueBinding + '\'' +
                ", mqQueueContext='" + mqQueueContext + '\'' +
                ", mqDebug=" + mqDebug +
                ", mqSSLoptions='" + mqSSLoptions + '\'' +
                ", mqSSLciphers='" + mqSSLciphers + '\'' +
                ", mqSSLkey='" + mqSSLkey + '\'' +
                ", mqSSLcaCert='" + mqSSLcaCert + '\'' +
                ", mqHeader='" + mqHeader + '\'' +
                ", mqSessionTimeout=" + mqSessionTimeout +
                ", routeInbound=" + routeInbound +
                ", routeOutbound=" + routeOutbound +
                ", inboundDir=" + inboundDir +
                ", inboundRoutingRule=" + inboundRoutingRule +
                ", inboundRequestorDN='" + inboundRequestorDN + '\'' +
                ", inboundResponderDN='" + inboundResponderDN + '\'' +
                ", inboundService='" + inboundService + '\'' +
                ", inboundType='" + inboundType + '\'' +
                ", inboundRequestType=" + Arrays.toString(inboundRequestType) +
                ", nonRepudiation=" + nonRepudiation +
                ", pauseInbound=" + pauseInbound +
                ", pauseOutbound=" + pauseOutbound +
                ", deleted=" + deleted +
                ", changeID='" + changeID + '\'' +
                ", changerComments='" + changerComments + '\'' +
                ", irishStep2=" + irishStep2 +
                ", e2eSigning='" + e2eSigning + '\'' +
                '}';
    }
}
