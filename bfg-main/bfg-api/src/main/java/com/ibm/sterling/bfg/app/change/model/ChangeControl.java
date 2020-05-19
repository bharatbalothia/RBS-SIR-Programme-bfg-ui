package com.ibm.sterling.bfg.app.change.model;

import com.ibm.sterling.bfg.app.model.ByteEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Table(name = "SFG_CHANGE_CONTROL")
@Entity
public class ChangeControl implements ChangeControlConstants {
    private static final long SERIAL_VERSION_UID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(com.ibm.sterling.bfg.app.model.Entity.class);

    @Id
    @Column(name = "CHANGE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SFG_CHANGE_CONTROL_IDSEQ")
    @GenericGenerator(
            name = "SFG_CHANGE_CONTROL_IDSEQ",
            strategy = "com.ibm.sterling.bfg.app.change.model.ChangeControlIdSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = ChangeControlIdSequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = ChangeControlIdSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "ID_"),
                    @org.hibernate.annotations.Parameter(name = ChangeControlIdSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%d") })
    @NotNull
    private String changeID; //pkey for the record

    @Column(name = "OPERATION")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Operation operation; //change operation - CREATE,UPDATE,DELETE
//    private String operation; //change operation - CREATE,UPDATE,DELETE

    @Column(name = "STATUS")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ChangeControlStatus status; //status of change - PENDING(0), REJECTED(1), ACCEPTED(2), FAILED(-1)

    @Column(name = "OBJECT_TYPE")
    private String objectType; //original SI object type being changed

    @Column(name = "OBJECT_KEY")
    private String objectKey; //Key for original SI Object being changed

    @Column(name = "CHANGE_USER")
    @NotNull
    private String changer; //user making the change

    @Column(name = "CHANGE_DATE")
    @NotNull
    @UpdateTimestamp
    private LocalDateTime dateChanged; //date change was requested

    @Column(name = "APPROVE_USER")
    private String approver; //user approving/rejecting the change

    @Column(name = "APPROVE_DATE")
    private String dateApproved; //date change was approved/rejected

    @Column(name = "CHANGE_COMMENTS")
    private String changerComments; //comments made by changer

    @Column(name = "APPROVE_COMMENTS")
    private String approverComments; //comments made by approver

    @Column(name = "ACTION_TYPE")
    private String actionType; //type of change e.g. XAPI_manageCertificae, CUSTOM_xyzClasss

    @Column(name = "ACTION_OBJECT")
    @Lob
    private byte[] actionObject; //DATA_ID for the action object that will fulfil the operation

    @Column(name = "RESULT_TYPE")
    private String resultType; //type of object view that displays when action is fulfilled

    @Column(name = "RESULT_OBJECT")
    @Lob
    private byte[] resultObject; //DATA_ID for the object view when the action is fulfilled

    @Column(name = "RESULT_META1")
    private String resultMeta1; //meta-data about the object when using CREATE action (searchable)

    @Column(name = "RESULT_META2")
    private String resultMeta2; //meta-data about the object when using CREATE action (searchable)

    @Column(name = "RESULT_META3")
    private String resultMeta3; //meta-data about the object when using CREATE action (searchable)

    public ChangeControl() {
        this.setStatus(ChangeControlStatus.PENDING);
        this.setObjectType(OBJECT_TYPE);
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
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = OBJECT_TYPE;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
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

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public byte[] getActionObject() {
        return actionObject;
    }

    public void setActionObject(byte[] actionObject) {
        this.actionObject = actionObject;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public byte[] getResultObject() {
        return resultObject;
    }

    public void setResultObject(byte[] resultObject) {
        this.resultObject = resultObject;
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

    public String getShortType(){
        String shortType = "Unknown";
        try {
            ChangeViewer changeViewer = (ChangeViewer) Class.forName(objectType).newInstance();
            shortType = changeViewer.getObjectType();
        } catch(Exception e){
        }
        return shortType;
    }

    public boolean isPending(){
        return status == ChangeControlStatus.PENDING;
    }

    @Override
    public String toString() {
        return "ChangeControl{" +
                "changeID='" + changeID + '\'' +
                ", operation='" + operation + '\'' +
                ", status=" + status +
                ", objectType='" + objectType + '\'' +
                ", objectKey='" + objectKey + '\'' +
                ", changer='" + changer + '\'' +
                ", dateChanged='" + dateChanged + '\'' +
                ", approver='" + approver + '\'' +
                ", dateApproved='" + dateApproved + '\'' +
                ", changerComments='" + changerComments + '\'' +
                ", approverComments='" + approverComments + '\'' +
                ", actionType='" + actionType + '\'' +
                ", actionObject=" + Arrays.toString(actionObject) +
                ", resultType='" + resultType + '\'' +
                ", resultObject=" + Arrays.toString(resultObject) +
                ", resultMeta1='" + resultMeta1 + '\'' +
                ", resultMeta2='" + resultMeta2 + '\'' +
                ", resultMeta3='" + resultMeta3 + '\'' +
                '}';
    }
}
