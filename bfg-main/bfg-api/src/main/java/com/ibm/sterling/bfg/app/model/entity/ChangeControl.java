package com.ibm.sterling.bfg.app.model.entity;

import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlIdSequenceGenerator;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.ObjectTypeConstants;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "SFG_CHANGE_CONTROL")
@Entity
public class ChangeControl implements ObjectTypeConstants, Comparable<ChangeControl>, Serializable, EntityType {
    private static final long SERIAL_VERSION_UID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(ChangeControl.class);

    @Id
    @Column(name = "CHANGE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SFG_CHANGE_CONTROL_IDSEQ")
    @GenericGenerator(
            name = "SFG_CHANGE_CONTROL_IDSEQ",
            strategy = "com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlIdSequenceGenerator",
            parameters = {
                    @Parameter(name = ChangeControlIdSequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = ChangeControlIdSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "ID_"),
                    @Parameter(name = ChangeControlIdSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%d")})
    @NotNull
    private String changeID; //pkey for the record

    @Column(name = "OPERATION")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Operation operation; //change operation - CREATE,UPDATE,DELETE

    @Column(name = "STATUS")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ChangeControlStatus status; //status of change - PENDING(0), REJECTED(1), ACCEPTED(2), FAILED(-1)

    @Column(name = "CHANGE_USER")
    @NotNull
    private String changer; //user making the change

    @Column(name = "CHANGE_DATE")
    @CreationTimestamp
    private Timestamp dateChanged; //date change was requested

    @Column(name = "APPROVE_USER")
    private String approver; //user approving/rejecting the change

    @Column(name = "APPROVE_DATE")
    @UpdateTimestamp
    private Timestamp dateApproved; //date change was approved/rejected

    @Column(name = "CHANGE_COMMENTS")
    private String changerComments; //comments made by changer

    @Column(name = "APPROVE_COMMENTS")
    private String approverComments; //comments made by approver

    @Column(name = "RESULT_META1")
    private String resultMeta1; //meta-data about the object when using CREATE action (searchable)

    @Column(name = "RESULT_META2")
    private String resultMeta2; //meta-data about the object when using CREATE action (searchable)

    @Column(name = "RESULT_META3")
    private String resultMeta3; //meta-data about the object when using CREATE action (searchable)

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ENTITY_LOG_ID")
    private EntityLog entityLog;

    public ChangeControl() {
        this.setStatus(ChangeControlStatus.PENDING);
    }

    public String getChangeID() {
        return changeID;
    }

    public void setChangeID(String changeID) {
        this.changeID = changeID;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public ChangeControlStatus getStatus() {
        return status;
    }

    public void setStatus(ChangeControlStatus status) {
        this.status = status;
    }

    public String getObjectType() {
        return OBJECT_TYPE_ENTITY;
    }

    public String getChanger() {
        return changer;
    }

    public void setChanger(String changer) {
        this.changer = changer;
    }

    public static long getSerialVersionUid() {
        return SERIAL_VERSION_UID;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getChangerComments() {
        return changerComments;
    }

    public void setChangerComments(String changerComments) {
        this.changerComments = changerComments;
    }

    public String getApproverComments() {
        return approverComments;
    }

    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }

    public String getResultMeta1() {
        return resultMeta1;
    }

    public void setResultMeta1(String resultMeta1) {
        this.resultMeta1 = resultMeta1;
    }

    public String getResultMeta2() {
        return resultMeta2;
    }

    public void setResultMeta2(String resultMeta2) {
        this.resultMeta2 = resultMeta2;
    }

    public String getResultMeta3() {
        return resultMeta3;
    }

    public void setResultMeta3(String resultMeta3) {
        this.resultMeta3 = resultMeta3;
    }

    public String getStatusText() {
        return status.getStatusText();
    }

    public EntityLog getEntityLog() {
        return entityLog;
    }

    public void setEntityLog(EntityLog entityLog) {
        this.entityLog = entityLog;
    }

    public com.ibm.sterling.bfg.app.model.entity.Entity convertEntityLogToEntity() {
        com.ibm.sterling.bfg.app.model.entity.Entity entityFromLog = new com.ibm.sterling.bfg.app.model.entity.Entity();
        entityFromLog.setEntityId(entityLog.getEntityId());
        entityFromLog.setEntity(entityLog.getEntity());
        entityFromLog.setService(entityLog.getService());
        entityFromLog.setMailboxPathOut(entityLog.getMailboxPathOut());
        entityFromLog.setMqQueueOut(entityLog.getMqQueueOut());
        entityFromLog.setRequestorDN(entityLog.getRequestorDN());
        entityFromLog.setResponderDN(entityLog.getResponderDN());
        entityFromLog.setServiceName(entityLog.getServiceName());
        entityFromLog.setRequestType(entityLog.getRequestType());
        entityFromLog.setSnF(entityLog.getSnF());
        entityFromLog.setTrace(entityLog.getTrace());
        entityFromLog.setDeliveryNotification(entityLog.getDeliveryNotification());
        entityFromLog.setDeliveryNotifDN(entityLog.getDeliveryNotifDN());
        entityFromLog.setDeliveryNotifRT(entityLog.getDeliveryNotifRT());
        entityFromLog.setRequestRef(entityLog.getRequestRef());
        entityFromLog.setFileDesc(entityLog.getFileDesc());
        entityFromLog.setFileInfo(entityLog.getFileInfo());
        entityFromLog.setTransferDesc(entityLog.getTransferDesc());
        entityFromLog.setTransferInfo(entityLog.getTransferInfo());
        entityFromLog.setCompression(entityLog.getCompression());
        entityFromLog.setMailboxPathIn(entityLog.getMailboxPathIn());
        entityFromLog.setMqQueueIn(entityLog.getMqQueueIn());
        entityFromLog.setEntityParticipantType(entityLog.getEntityParticipantType());
        entityFromLog.setDirectParticipant(entityLog.getDirectParticipant());
        entityFromLog.setMaxTransfersPerBulk(entityLog.getMaxTransfersPerBulk());
        entityFromLog.setMaxBulksPerFile(entityLog.getMaxBulksPerFile());
        entityFromLog.setStartOfDay(entityLog.getStartOfDay());
        entityFromLog.setEndOfDay(entityLog.getEndOfDay());
        entityFromLog.setCdNode(entityLog.getCdNode());
        entityFromLog.setIdfWTOMsgId(entityLog.getIdfWTOMsgId());
        entityFromLog.setCdfWTOMsgId(entityLog.getCdfWTOMsgId());
        entityFromLog.setSdfWTOMsgId(entityLog.getSdfWTOMsgId());
        entityFromLog.setRsfWTOMsgId(entityLog.getRsfWTOMsgId());
        entityFromLog.setDnfWTOMsgId(entityLog.getDnfWTOMsgId());
        entityFromLog.setDvfWTOMsgId(entityLog.getDvfWTOMsgId());
        entityFromLog.setMsrWTOMsgId(entityLog.getMsrWTOMsgId());
        entityFromLog.setPsrWTOMsgId(entityLog.getPsrWTOMsgId());
        entityFromLog.setDrrWTOMsgId(entityLog.getDrrWTOMsgId());
        entityFromLog.setRtfWTOMsgId(entityLog.getRtfWTOMsgId());
        entityFromLog.setMbpWTOMsgId(entityLog.getMbpWTOMsgId());
        entityFromLog.setMqHost(entityLog.getMqHost());
        entityFromLog.setMqPort(entityLog.getMqPort());
        entityFromLog.setMqQManager(entityLog.getMqQManager());
        entityFromLog.setMqChannel(entityLog.getMqChannel());
        entityFromLog.setMqQueueName(entityLog.getMqQueueName());
        entityFromLog.setMqQueueBinding(entityLog.getMqQueueBinding());
        entityFromLog.setMqQueueContext(entityLog.getMqQueueContext());
        entityFromLog.setMqDebug(entityLog.getMqDebug());
        entityFromLog.setMqSSLOptions(entityLog.getMqSSLOptions());
        entityFromLog.setMqSSLCiphers(entityLog.getMqSSLCiphers());
        entityFromLog.setMqSSLKeyCert(entityLog.getMqSSLKeyCert());
        entityFromLog.setMqSSLCaCert(entityLog.getMqSSLCaCert());
        entityFromLog.setMqHeader(entityLog.getMqHeader());
        entityFromLog.setMqSessionTimeout(entityLog.getMqSessionTimeout());
        entityFromLog.setInboundRequestorDN(entityLog.getInboundRequestorDN());
        entityFromLog.setInboundResponderDN(entityLog.getInboundResponderDN());
        entityFromLog.setInboundService(entityLog.getInboundService());
        entityFromLog.setNonRepudiation(entityLog.getNonRepudiation());
        entityFromLog.setPauseInbound(entityLog.getPauseInbound());
        entityFromLog.setPauseOutbound(entityLog.getPauseOutbound());
        entityFromLog.setE2eSigning(entityLog.getE2eSigning());
        entityFromLog.setRouteInbound(entityLog.getRouteInbound());
        entityFromLog.setRouteOutbound(entityLog.getRouteOutbound());
        entityFromLog.setInboundRequestType(entityLog.getInboundRequestType());
        entityFromLog.setChangeID(changeID);
        entityFromLog.setChangerComments(changerComments);
        entityFromLog.setIrishStep2(entityLog.getIrishStep2());
        entityFromLog.setSchedules(entityLog.getSchedules());
        return entityFromLog;
    }

    public boolean isPending() {
        return status == ChangeControlStatus.PENDING;
    }

    @Override
    public String toString() {
        return "ChangeControl{" +
                "changeID='" + changeID + '\'' +
                ", operation='" + operation + '\'' +
                ", status=" + status +
                ", changer='" + changer + '\'' +
                ", dateChanged='" + dateChanged + '\'' +
                ", approver='" + approver + '\'' +
                ", dateApproved='" + dateApproved + '\'' +
                ", changerComments='" + changerComments + '\'' +
                ", approverComments='" + approverComments + '\'' +
                ", resultMeta1='" + resultMeta1 + '\'' +
                ", resultMeta2='" + resultMeta2 + '\'' +
                ", resultMeta3='" + resultMeta3 + '\'' +
                '}';
    }

    public Timestamp getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Timestamp dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Timestamp getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Timestamp dateApproved) {
        this.dateApproved = dateApproved;
    }

    @Override
    public int compareTo(ChangeControl changeControl) {
        return resultMeta1.compareToIgnoreCase(changeControl.getResultMeta1());
    }

    @Override
    public String nameForSorting() {
        return resultMeta1;
    }

}
