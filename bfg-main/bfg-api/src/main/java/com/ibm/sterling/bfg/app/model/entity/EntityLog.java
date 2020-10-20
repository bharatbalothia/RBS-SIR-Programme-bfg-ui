package com.ibm.sterling.bfg.app.model.entity;

import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlIdSequenceGenerator;
import com.ibm.sterling.bfg.app.utils.DebugStringToIntegerConverter;
import com.ibm.sterling.bfg.app.utils.StringTimeToIntegerMinuteConverter;
import com.ibm.sterling.bfg.app.utils.StringToListConverter;
import com.ibm.sterling.bfg.app.utils.StringToScheduleListConverter;
import com.ibm.sterling.bfg.app.utils.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.sterling.bfg.app.utils.FieldCheckUtil.checkStringEmptyOrNull;

@Entity
@Table(name = "SCT_ENTITY_LOG")
public class EntityLog {
    private static final Logger LOG = LogManager.getLogger(EntityLog.class);

    @Id
    @Column(name = "ENTITY_LOG_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_ENTITY_LOG_IDSEQ")
    @GenericGenerator(
            name = "SCT_ENTITY_LOG_IDSEQ",
            strategy = "com.ibm.sterling.bfg.app.model.changeControl.ChangeControlIdSequenceGenerator",
            parameters = {
                    @Parameter(name = ChangeControlIdSequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = ChangeControlIdSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "ENTITY_LOG_ID_"),
                    @Parameter(name = ChangeControlIdSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%d")})
    private String entityLogId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @NotBlank(message = "ENTITY has to be present")
    private String entity;
    @NotBlank(message = "SERVICE has to be present")
    private String service;
    @Column(name = "MAILBOXPATHOUT")
    @NotBlank(message = "MAILBOXPATHOUT has to be present")
    private String mailboxPathOut;
    @Column(name = "MQQUEUEOUT")
    private String mqQueueOut;
    @Column(name = "ROUTE_INBOUND")
    private Boolean routeInbound;
    @Column(name = "ROUTE_OUTBOUND")
    private Boolean routeOutbound;
    @Column(name = "INBOUND_DIR")
    private Boolean inboundDir;
    @Column(name = "INBOUND_ROUTING_RULE")
    private Boolean inboundRoutingRule;
    @Column(name = "ISDELETED")
    private Boolean deleted;
    @Column(name = "REQUESTORDN")
    private String requestorDN;
    @Column(name = "RESPONDERDN")
    private String responderDN;
    @Column(name = "SERVICENAME")
    private String serviceName;
    @Column(name = "REQUESTTYPE")
    private String requestType;
    @Column(name = "SNF")
    @NotNull(message = "SNF has to be present")
    private Boolean snF = Boolean.FALSE;
    @NotNull(message = "TRACE has to be present")
    private Boolean trace = Boolean.FALSE;
    @Column(name = "DELIVERYNOTIF")
    @NotNull(message = "DELIVERYNOTIF has to be present")
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
    @NotNull(message = "COMPRESSION has to be present")
    private Boolean compression = Boolean.FALSE;
    @Column(name = "MAILBOXPATHIN")
    @NotBlank(message = "MAILBOXPATHIN has to be present")
    private String mailboxPathIn;
    @Column(name = "MQQUEUEIN")
    private String mqQueueIn;
    @Column(name = "ENTITY_PARTICIPANT_TYPE")
    private String entityParticipantType;
    @Column(name = "DIRECT_PARTICIPANT")
    private String directParticipant;
    @Column(name = "MAXTRANSPERBULK")
    @NotNull(message = "MAXTRANSPERBULK has to be present")
    private Integer maxTransfersPerBulk;
    @Column(name = "MAXBULKSPERFILE")
    @NotNull(message = "MAXBULKSPERFILE has to be present")
    private Integer maxBulksPerFile;
    @Column(name = "STARTOFDAY")
    @NotNull(message = "STARTOFDAY has to be present")
    @Convert(converter = StringTimeToIntegerMinuteConverter.class)
    private String startOfDay;
    @Column(name = "ENDOFDAY")
    @NotNull(message = "ENDOFDAY has to be present")
    @Convert(converter = StringTimeToIntegerMinuteConverter.class)
    private String endOfDay;
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
    @Convert(converter = DebugStringToIntegerConverter.class)
    @Column(name = "MQ_DEBUG")
    private String mqDebug;
    @Column(name = "MQ_SSLOPTION")
    private String mqSSLOptions;
    @Column(name = "MQ_SSLCIPHERS")
    private String mqSSLCiphers;
    @Column(name = "MQ_SSLSYSTEMCERTID")
    private String mqSSLKeyCert;
    @Column(name = "MQ_SSLCACERTID")
    private String mqSSLCaCert;
    @Column(name = "MQ_HEADER")
    private String mqHeader;
    @Column(name = "MQ_SESSIONTIMEOUT")
    private Integer mqSessionTimeout;
    @Column(name = "ROUTE_REQUESTORDN")
    private String inboundRequestorDN = "";
    @Column(name = "ROUTE_RESPONDERDN")
    private String inboundResponderDN = "";
    @Column(name = "ROUTE_SERVICE")
    private String inboundService = "";
    @Column(name = "NONREPUDIATION")
    @NotNull(message = "NONREPUDIATION has to be present")
    private Boolean nonRepudiation;
    @Column(name = "PAUSE_INBOUND")
    @NotNull(message = "PAUSE_INBOUND has to be present")
    private Boolean pauseInbound;
    @Column(name = "PAUSE_OUTBOUND")
    @NotNull(message = "PAUSE_OUTBOUND has to be present")
    private Boolean pauseOutbound;
    @Column(name = "E2ESIGNING")
    private String e2eSigning;

    @Convert(converter = StringToListConverter.class)
    @Column(name = "ROUTE_REQUESTTYPE")
    private List<String> inboundRequestType = new ArrayList<>();

    @Column(name = "IRISH_STEP2")
    private Boolean irishStep2;

    @Convert(converter = StringToScheduleListConverter.class)
    @Column(name = "SCHEDULES", columnDefinition = "varchar2(4000)")
    private List<Schedule> schedules;

    public EntityLog() {
    }

    public EntityLog(com.ibm.sterling.bfg.app.model.entity.Entity entity) {
        this.entity = entity.getEntity();
        this.entityId = entity.getEntityId();
        this.requestorDN = entity.getRequestorDN();
        this.responderDN = entity.getResponderDN();
        this.serviceName = entity.getServiceName();
        this.requestType = entity.getRequestType();
        this.snF = entity.getSnF();
        this.trace = entity.getTrace();
        this.deliveryNotification = entity.getDeliveryNotification();
        this.deliveryNotifDN = entity.getDeliveryNotifDN();
        this.deliveryNotifRT = entity.getDeliveryNotifRT();
        this.requestRef = entity.getRequestRef();
        this.fileDesc = entity.getFileDesc();
        this.fileInfo = entity.getFileInfo();
        this.transferDesc = entity.getTransferDesc();
        this.transferInfo = entity.getTransferInfo();
        this.compression = entity.getCompression();
        this.mailboxPathIn = entity.getMailboxPathIn();
        this.mqQueueIn = entity.getMqQueueIn();
        this.entityParticipantType = entity.getEntityParticipantType();
        this.directParticipant = entity.getDirectParticipant();
        this.maxTransfersPerBulk = entity.getMaxTransfersPerBulk();
        this.maxBulksPerFile = entity.getMaxBulksPerFile();
        this.startOfDay = entity.getStartOfDay();
        this.endOfDay = entity.getEndOfDay();
        this.cdNode = entity.getCdNode();
        this.idfWTOMsgId = entity.getIdfWTOMsgId();
        this.cdfWTOMsgId = entity.getCdfWTOMsgId();
        this.sdfWTOMsgId = entity.getSdfWTOMsgId();
        this.rsfWTOMsgId = entity.getRsfWTOMsgId();
        this.dnfWTOMsgId = entity.getDnfWTOMsgId();
        this.dvfWTOMsgId = entity.getDvfWTOMsgId();
        this.msrWTOMsgId = entity.getMsrWTOMsgId();
        this.psrWTOMsgId = entity.getPsrWTOMsgId();
        this.drrWTOMsgId = entity.getDrrWTOMsgId();
        this.rtfWTOMsgId = entity.getRtfWTOMsgId();
        this.mbpWTOMsgId = entity.getMbpWTOMsgId();
        this.mqHost = entity.getMqHost();
        this.mqPort = entity.getMqPort();
        this.mqQManager = entity.getMqQManager();
        this.mqChannel = entity.getMqChannel();
        this.mqQueueName = entity.getMqQueueName();
        this.mqQueueBinding = entity.getMqQueueBinding();
        this.mqQueueContext = entity.getMqQueueContext();
        this.mqDebug = entity.getMqDebug();
        this.mqSSLOptions = entity.getMqSSLOptions();
        this.mqSSLCiphers = entity.getMqSSLCiphers();
        this.mqSSLKeyCert = entity.getMqSSLKeyCert();
        this.mqSSLCaCert = entity.getMqSSLCaCert();
        this.mqHeader = entity.getMqHeader();
        this.mqSessionTimeout = entity.getMqSessionTimeout();
        this.inboundRequestorDN = entity.getInboundRequestorDN();
        this.inboundResponderDN = entity.getInboundResponderDN();
        this.inboundService = entity.getInboundService();
        this.nonRepudiation = entity.getNonRepudiation();
        this.pauseInbound = entity.getPauseInbound();
        this.pauseOutbound = entity.getPauseOutbound();
        this.e2eSigning = entity.getE2eSigning();
        this.service = entity.getService();
        this.mailboxPathOut = entity.getMailboxPathOut();
        this.mqQueueOut = entity.getMqQueueOut();
        this.routeInbound = entity.getRouteInbound();
        this.routeOutbound = entity.getRouteOutbound();
        this.inboundDir = entity.getInboundDir();
        this.inboundRoutingRule = entity.getInboundRoutingRule();
        this.deleted = entity.getDeleted();
        this.inboundRequestType = entity.getInboundRequestType();
        this.irishStep2 = entity.getIrishStep2();
        this.schedules = entity.getSchedules();
        this.schedules.forEach(
                schedule -> schedule.setNextRun(TimeUtil.convertTimeToLocalDateTime(schedule.getTimeStart())));
    }

    @PrePersist
    @PreUpdate
    public void init() {
        LOG.debug("Setting {} + {} defaults for mailbox MQ and SWIFT fields.", entity, service);
        if (checkStringEmptyOrNull(mailboxPathIn)) mailboxPathIn = entity + "_" + service;
        if (checkStringEmptyOrNull(mailboxPathOut)) mailboxPathOut = entity + "_" + service;
        if (checkStringEmptyOrNull(mqQueueIn)) mqQueueIn = entity + "_" + service;
        if (checkStringEmptyOrNull(mqQueueOut)) mqQueueOut = entity + "_" + service;
    }

    public String getEntityLogId() {
        return entityLogId;
    }

    public void setEntityLogId(String entityLogId) {
        this.entityLogId = entityLogId;
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

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getMailboxPathOut() {
        return mailboxPathOut;
    }

    public void setMailboxPathOut(String mailboxPathOut) {
        this.mailboxPathOut = mailboxPathOut;
    }

    public String getMqQueueOut() {
        return mqQueueOut;
    }

    public void setMqQueueOut(String mqQueueOut) {
        this.mqQueueOut = mqQueueOut;
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

    public List<String> getInboundRequestType() {
        return inboundRequestType;
    }

    public void setInboundRequestType(List<String> inboundRequestType) {
        this.inboundRequestType = inboundRequestType;
    }

    public Boolean getIrishStep2() {
        return irishStep2;
    }

    public void setIrishStep2(Boolean irishStep2) {
        this.irishStep2 = irishStep2;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
        return snF;
    }

    public void setSnF(Boolean snF) {
        this.snF = snF;
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

    public String getMqQueueIn() {
        return mqQueueIn;
    }

    public void setMqQueueIn(String mqQueueIn) {
        this.mqQueueIn = mqQueueIn;
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

    public String getStartOfDay() {
        return startOfDay;
    }

    public void setStartOfDay(String startOfDay) {
        this.startOfDay = startOfDay;
    }

    public String getEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(String endOfDay) {
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

    public String getMqDebug() {
        return mqDebug;
    }

    public void setMqDebug(String mqDebug) {
        this.mqDebug = mqDebug;
    }

    public String getMqSSLOptions() {
        return mqSSLOptions;
    }

    public void setMqSSLOptions(String mqSSLOptions) {
        this.mqSSLOptions = mqSSLOptions;
    }

    public String getMqSSLCiphers() {
        return mqSSLCiphers;
    }

    public void setMqSSLCiphers(String mqSSLCiphers) {
        this.mqSSLCiphers = mqSSLCiphers;
    }

    public String getMqSSLKeyCert() {
        return mqSSLKeyCert;
    }

    public void setMqSSLKeyCert(String mqSSLKeyCert) {
        this.mqSSLKeyCert = mqSSLKeyCert;
    }

    public String getMqSSLCaCert() {
        return mqSSLCaCert;
    }

    public void setMqSSLCaCert(String mqSSLCaCert) {
        this.mqSSLCaCert = mqSSLCaCert;
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

    public String getE2eSigning() {
        return e2eSigning;
    }

    public void setE2eSigning(String e2eSigning) {
        this.e2eSigning = e2eSigning;
    }

    @Override
    public String toString() {
        return "EntityLog{" +
                "entityLogId='" + entityLogId + '\'' +
                ", entityId=" + entityId +
                ", entity='" + entity + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
