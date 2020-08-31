package com.ibm.sterling.bfg.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ibm.sterling.bfg.app.model.validation.GplValidation;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.MQValid;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.SctValidation;
import com.ibm.sterling.bfg.app.model.validation.unique.EntityUnique;
import com.ibm.sterling.bfg.app.model.validation.unique.EntityUpdateUniqueness;
import com.ibm.sterling.bfg.app.model.validation.unique.EntityServiceUniquenessConstraint;
import com.ibm.sterling.bfg.app.service.EntityService;
import com.ibm.sterling.bfg.app.utils.DebugStringToIntegerConverter;
import com.ibm.sterling.bfg.app.utils.StringTimeToIntegerMinuteConverter;
import com.ibm.sterling.bfg.app.utils.StringToListConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.sterling.bfg.app.utils.FieldCheckUtil.checkStringEmptyOrNull;

@EntityServiceUniquenessConstraint(groups = {GplValidation.PostValidation.class, SctValidation.PostValidation.class,})
@EntityUpdateUniqueness(groups = {GplValidation.PutValidation.class, SctValidation.PutValidation.class})
@MQValid(groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})
@javax.persistence.Entity
@Table(name = "SCT_ENTITY")
public class Entity implements EntityType {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(Entity.class);

    @Id
    @Column(name = "ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_ENTITY_IDSEQ")
    @SequenceGenerator(sequenceName = "SCT_ENTITY_IDSEQ", name = "SCT_ENTITY_IDSEQ", allocationSize = 1)
    private Integer entityId;

    @NotBlank(message = "ENTITY has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Pattern.List({
            @Pattern(regexp = "^([a-zA-Z]){4}([a-zA-Z]){2}([0-9a-zA-Z]){2}([0-9a-zA-Z]{3})$",
                    message = "Entity should be in BIC11 format",
                    groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class}),
            @Pattern(regexp = "^[A-Z]{6}[A-Z2-9][A-NP-Z0-9]$",
                    message = "Entity should be in BIC8 format",
                    groups = {SctValidation.PostValidation.class, SctValidation.PutValidation.class})})
    @Column
    private String entity;

    @NotBlank(message = "SERVICE has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column
    private String service;

    @Pattern(
            regexp = "^(?:(?:(?:(?:cn|ou)=[^,]+,?)+),[\\s]*)*(?:o=[a-z]{6}[0-9a-z]{2}){1},[\\s]*o=swift$",
            message = "Please match the requested format for RequestorDN",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "REQUESTORDN")
    private String requestorDN;

    @Pattern(
            regexp = "^(?:(?:(?:(?:cn|ou)=[^,]+,?)+),[\\s]*)*(?:o=[a-z]{6}[0-9a-z]{2}){1},[\\s]*o=swift$",
            message = "Please match the requested format for ResponderDN",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "RESPONDERDN")
    private String responderDN;

    @Column(name = "SERVICENAME")
    private String serviceName;
    @Column(name = "REQUESTTYPE")
    private String requestType;

    @Column(name = "SNF")
    @NotNull(message = "SNF has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    private Boolean SnF = Boolean.FALSE;

    @NotNull(message = "TRACE has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    private Boolean trace = Boolean.FALSE;

    @NotNull(message = "DELIVERYNOTIF has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
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
    @NotNull(message = "COMPRESSION has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "COMPRESSION")
    private Boolean compression = Boolean.FALSE;
    @Column(name = "MAILBOXPATHIN")
    private String mailboxPathIn = "";
    @EntityUnique(
            service = EntityService.class,
            fieldName = "MAILBOXPATHOUT",
            message = "MAILBOXPATHOUT has to be unique",
            groups = {GplValidation.PostValidation.class, SctValidation.PostValidation.class,})
    @Column(name = "MAILBOXPATHOUT")
    private String mailboxPathOut = "";
    @Column(name = "MQQUEUEIN")
    private String mqQueueIn;
    @EntityUnique(
            service = EntityService.class,
            fieldName = "MQQUEUEOUT",
            message = "MQQUEUEOUT has to be unique",
            groups = {GplValidation.PostValidation.class, SctValidation.PostValidation.class,})
    @Column(name = "MQQUEUEOUT")
    private String mqQueueOut;

    @Column(name = "ENTITY_PARTICIPANT_TYPE")
    private String entityParticipantType;
    @Column(name = "DIRECT_PARTICIPANT")
    private String directParticipant;

    @NotNull(message = "MAXTRANSPERBULK has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "MAXTRANSPERBULK")
    private Integer maxTransfersPerBulk = 0;
    @NotNull(message = "MAXBULKSPERFILE has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "MAXBULKSPERFILE")
    private Integer maxBulksPerFile = 0;

    @NotNull(message = "STARTOFDAY has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Pattern(
            regexp = "^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])",
            message = "Start of day should be in hh:mm format",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Convert(converter = StringTimeToIntegerMinuteConverter.class)
    @Column(name = "STARTOFDAY")
    private String startOfDay = "00:00";

    @Pattern(
            regexp = "^([0-1]?[0-9]|[2][0-3]):([0-5][0-9])",
            message = "End of day should be in hh:mm format",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @NotNull(message = "ENDOFDAY has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Convert(converter = StringTimeToIntegerMinuteConverter.class)
    @Column(name = "ENDOFDAY")
    private String endOfDay = "00:00";

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

    @Transient
    private Boolean routeInbound = Boolean.TRUE;
    @Transient
    private Boolean routeOutbound = Boolean.TRUE;
    @Transient
    private Boolean inboundDir = Boolean.FALSE;
    @Transient
    private Boolean inboundRoutingRule = Boolean.FALSE;

    @Pattern(
            regexp = "^(?:(?:(?:(?:cn|ou)=[^,]+,?)+),[\\s]*)*(?:o=[a-z]{6}[0-9a-z]{2}){1},[\\s]*o=swift$",
            message = "Please match the requested format for Inbound RequestorDN",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class})
    @Column(name = "ROUTE_REQUESTORDN")
    private String inboundRequestorDN = "";

    @Pattern(
            regexp = "^(?:(?:(?:(?:cn|ou)=[^,]+,?)+),[\\s]*)*(?:o=[a-z]{6}[0-9a-z]{2}){1},[\\s]*o=swift$",
            message = "Please match the requested format for Inbound ResponderDN",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class})
    @Column(name = "ROUTE_RESPONDERDN")
    private String inboundResponderDN = "";

    @Column(name = "ROUTE_SERVICE")
    private String inboundService = "";
    @Convert(converter = StringToListConverter.class)
    @Column(name = "ROUTE_REQUESTTYPE")
    private List<String> inboundRequestType = new ArrayList<>();
    @NotNull(message = "NONREPUDIATION has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "NONREPUDIATION")
    private Boolean nonRepudiation = Boolean.FALSE;
    @NotNull(message = "PAUSE_INBOUND has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
    @Column(name = "PAUSE_INBOUND")
    private Boolean pauseInbound = Boolean.FALSE;
    @NotNull(message = "PAUSE_OUTBOUND has to be present",
            groups = {GplValidation.PostValidation.class, GplValidation.PutValidation.class,
                    SctValidation.PostValidation.class, SctValidation.PutValidation.class})
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
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "entity",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Valid
    private List<Schedule> schedules = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void init() {
        LOG.debug("Setting {} + {} defaults for mailbox MQ and SWIFT fields.", entity, service);
        if (checkStringEmptyOrNull(mailboxPathIn)) mailboxPathIn = entity + "_" + service;
        if (checkStringEmptyOrNull(mailboxPathOut)) mailboxPathOut = entity + "_" + service;
        if (checkStringEmptyOrNull(mqQueueIn)) mqQueueIn = entity + "_" + service;
        if (checkStringEmptyOrNull(mqQueueOut)) mqQueueOut = entity + "_" + service;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static Logger getLog() {
        return LOG;
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

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
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

    public List<String> getInboundRequestType() {
        return inboundRequestType;
    }

    public void setInboundRequestType(List<String> inboundRequestType) {
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

    @Override
    public String nameForSorting() {
        return entity;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entityId=" + entityId +
                ", entity='" + entity + '\'' +
                ", service='" + service + '\'' + '}';
    }

}
