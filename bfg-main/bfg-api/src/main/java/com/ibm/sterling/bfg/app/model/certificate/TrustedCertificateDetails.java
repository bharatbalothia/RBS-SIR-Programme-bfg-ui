package com.ibm.sterling.bfg.app.model.certificate;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

public class TrustedCertificateDetails {

    private String serialNumber;
    private String thumbprint;
    private String thumbprint256;
    private String startDate;
    private String endDate;
    private Map<String, List<String>> issuer;
    private Map<String, List<String>> subject;
    private List<Map<String, String>> authChainReport = null;
    private boolean valid = true;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, List<String>>> certificateErrors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, Object>> certificateWarnings;

    public TrustedCertificateDetails() {
    }

    public TrustedCertificateDetails(TrustedCertificateDetails trustedCertificateDetails) {
        this.serialNumber = trustedCertificateDetails.getSerialNumber();
        this.thumbprint = trustedCertificateDetails.getThumbprint();
        this.thumbprint256 = trustedCertificateDetails.getThumbprint256();
        this.startDate = trustedCertificateDetails.getStartDate();
        this.endDate = trustedCertificateDetails.getEndDate();
        this.issuer = trustedCertificateDetails.getIssuer();
        this.subject = trustedCertificateDetails.getSubject();
        this.authChainReport = trustedCertificateDetails.getAuthChainReport();
        this.valid = trustedCertificateDetails.isValid();
        this.certificateErrors = trustedCertificateDetails.getCertificateErrors();
        this.certificateWarnings = trustedCertificateDetails.getCertificateWarnings();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public String getThumbprint256() {
        return thumbprint256;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Map<String, List<String>> getIssuer() {
        return issuer;
    }

    public Map<String, List<String>> getSubject() {
        return subject;
    }

    public List<Map<String, String>> getAuthChainReport() {
        return authChainReport;
    }

    public boolean isValid() {
        return valid;
    }

    public List<Map<String, List<String>>> getCertificateErrors() {
        return certificateErrors;
    }

    public List<Map<String, Object>> getCertificateWarnings() {
        return certificateWarnings;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }

    public void setThumbprint256(String thumbprint256) {
        this.thumbprint256 = thumbprint256;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setIssuer(Map<String, List<String>> issuer) {
        this.issuer = issuer;
    }

    public void setSubject(Map<String, List<String>> subject) {
        this.subject = subject;
    }

    public void setAuthChainReport(List<Map<String, String>> authChainReport) {
        this.authChainReport = authChainReport;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setCertificateErrors(List<Map<String, List<String>>> certificateErrors) {
        this.certificateErrors = certificateErrors;
    }

    public void setCertificateWarnings(List<Map<String, Object>> certificateWarnings) {
        this.certificateWarnings = certificateWarnings;
    }

    public TrustedCertificate convertToTrustedCertificate() {
        TrustedCertificate certificate = new TrustedCertificate();
        certificate.setSerialNumber(this.serialNumber);
        certificate.setThumbprint(this.thumbprint);
        certificate.setThumbprint256(this.thumbprint256);
        certificate.setStartDate(this.startDate);
        certificate.setEndDate(this.endDate);
        certificate.setIssuer(this.issuer);
        certificate.setSubject(this.subject);
        certificate.setAuthChainReport(this.authChainReport);
        return certificate;
    }

}
