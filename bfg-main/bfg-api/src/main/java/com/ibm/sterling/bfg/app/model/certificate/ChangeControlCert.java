package com.ibm.sterling.bfg.app.model.certificate;

import com.ibm.sterling.bfg.app.model.CertType;
import com.ibm.sterling.bfg.app.model.changeControl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "FB_CHANGE_CONTROL")
@Entity
public class ChangeControlCert implements ChangeControlConstants, Comparable<ChangeControl>, Serializable, CertType {
    private static final long SERIAL_VERSION_UID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(ChangeControlCert.class);

    @Id
    @Column(name = "CHANGE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SFG_CHANGE_CONTROL_IDSEQ")
    @GenericGenerator(
            name = "SFG_CHANGE_CONTROL_IDSEQ",
            strategy = "com.ibm.sterling.bfg.app.model.changeControl.ChangeControlIdSequenceGenerator",
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CERT_LOG_ID")
    private TrustedCertificateLog trustedCertificateLog;

    public ChangeControlCert() {
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

    public String getStatusText() {
        return status.getStatusText();
    }

    public TrustedCertificateLog getTrustedCertificateLog() {
        return trustedCertificateLog;
    }

    public void setTrustedCertificateLog(TrustedCertificateLog trustedCertificateLog) {
        this.trustedCertificateLog = trustedCertificateLog;
    }

    public TrustedCertificate convertTrustedCertificateLogToTrustedCertificate() {
        TrustedCertificate certFromLog = new TrustedCertificate();
        certFromLog.setCertificateId(trustedCertificateLog.getCertificateId());
        certFromLog.setCertificateName(trustedCertificateLog.getCertificateName());
        certFromLog.setSerialNumber(trustedCertificateLog.getSerialNumber());
        certFromLog.setCertificateThumbprint(trustedCertificateLog.getCertificateThumbprint());
        certFromLog.setCertificate(trustedCertificateLog.getCertificate());
        certFromLog.setStartDate(trustedCertificateLog.getStartDate());
        certFromLog.setEndDate(trustedCertificateLog.getEndDate());
        certFromLog.setIssuer(trustedCertificateLog.getIssuer());
        certFromLog.setSubject(trustedCertificateLog.getSubject());
        certFromLog.setChangeID(changeID);
        certFromLog.setChangerComments(changerComments);
        return certFromLog;
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
                ", changer='" + changer + '\'' +
                ", dateChanged='" + dateChanged + '\'' +
                ", approver='" + approver + '\'' +
                ", dateApproved='" + dateApproved + '\'' +
                ", changerComments='" + changerComments + '\'' +
                ", approverComments='" + approverComments + '\'' +
                ", resultMeta1='" + resultMeta1 + '\'' +
                ", resultMeta2='" + resultMeta2 + '}';
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
    public int compareTo(ChangeControl сс) {
        return resultMeta1.toLowerCase().compareTo(сс.getResultMeta1().toLowerCase());
    }

    @Override
    public String nameForSorting() {
        return resultMeta1;
    }
}
