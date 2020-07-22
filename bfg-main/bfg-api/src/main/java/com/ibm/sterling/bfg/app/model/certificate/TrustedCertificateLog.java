package com.ibm.sterling.bfg.app.model.certificate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlIdSequenceGenerator;
import com.ibm.sterling.bfg.app.utils.StringToMapConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

@Table(name = "SCT_TRUSTED_CERTIFICATE_LOG")
@Entity
public class TrustedCertificateLog {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(TrustedCertificateLog.class);

    @Id
    @Column(name = "CERT_LOG_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCT_TRUSTED_CERT_LOG_IDSEQ")
    @GenericGenerator(
            name = "SCT_TRUSTED_CERT_LOG_IDSEQ",
            strategy = "com.ibm.sterling.bfg.app.model.changeControl.ChangeControlIdSequenceGenerator",
            parameters = {
                    @Parameter(name = ChangeControlIdSequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = ChangeControlIdSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "CERT_LOG_ID_"),
                    @Parameter(name = ChangeControlIdSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%d")})
    private String certificateLogId;

    @Column(name = "CERT_ID")
    private String certificateId;

    @Column(name = "CERTIFICATE_NAME")
    private String certificateName;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "THUMBPRINT")
    private String thumbprint;

    @Column(name = "THUMBPRINT256")
    private String thumbprint256;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "ISSUER")
    @Convert(converter = StringToMapConverter.class)
    private Map<String, List<String>> issuer;

    @Column(name = "SUBJECT")
    @Convert(converter = StringToMapConverter.class)
    private Map<String, List<String>> subject;

    @Transient
    private List<Map<String, String>> authChainReport;

    @JsonIgnore
    @Column(name = "CERTIFICATE")
    @Lob
    private X509Certificate certificate;

    @JsonBackReference
    @OneToOne(mappedBy = "trustedCertificateLog")
    private ChangeControlCert controlCert;

    public TrustedCertificateLog() {
    }

    public TrustedCertificateLog(TrustedCertificate trustedCertificate) {
        this.certificateId = trustedCertificate.getCertificateId();
        this.certificateName = trustedCertificate.getCertificateName();
        this.serialNumber = trustedCertificate.getSerialNumber();
        this.thumbprint = trustedCertificate.getThumbprint();
        this.thumbprint256 = trustedCertificate.getThumbprint256();
        this.startDate = trustedCertificate.getStartDate();
        this.endDate = trustedCertificate.getEndDate();
        this.issuer = trustedCertificate.getIssuer();
        this.subject = trustedCertificate.getSubject();
        this.authChainReport = trustedCertificate.getAuthChainReport();
        this.certificate = trustedCertificate.getCertificate();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public String getCertificateLogId() {
        return certificateLogId;
    }

    public void setCertificateLogId(String certificateLogId) {
        this.certificateLogId = certificateLogId;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }

    public String getThumbprint256() {
        return thumbprint256;
    }

    public void setThumbprint256(String thumbprint256) {
        this.thumbprint256 = thumbprint256;
    }

    public ChangeControlCert getControlCert() {
        return controlCert;
    }

    public void setControlCert(ChangeControlCert controlCert) {
        this.controlCert = controlCert;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, List<String>> getIssuer() {
        return issuer;
    }

    public void setIssuer(Map<String, List<String>> issuer) {
        this.issuer = issuer;
    }

    public Map<String, List<String>> getSubject() {
        return subject;
    }

    public void setSubject(Map<String, List<String>> subject) {
        this.subject = subject;
    }

    public List<Map<String, String>> getAuthChainReport() {
        return authChainReport;
    }

    public void setAuthChainReport(List<Map<String, String>> authChainReport) {
        this.authChainReport = authChainReport;
    }

}
