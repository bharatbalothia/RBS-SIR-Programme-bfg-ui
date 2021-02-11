package com.ibm.sterling.bfg.app.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "property")
public class PropertySettings {

    private String reqTypePrefixKey;
    private String inboundServiceKey;
    private String swiftServiceKey;
    private String fileTypeKey;
    private String mqPrefixKey;
    private String gplUrl;
    private String bfgUiUrl;
    private String caCertUrl;
    private String sysCertUrl;
    private String certIdKey;
    private String fileSearchPrefixKey;
    private String fileUrl;
    private String fileStatusPrefixKey;
    private String transactionStatusPrefixKey;
    private String[] fileSearchPostfixKey;
    private String fileErrorUrl;
    private String[] fileErrorPostfixKey;
    private String transactionSearchPrefixKey;
    private String[] transactionSearchPostfixKey;
    private String useraccountGroupsKey;
    private String useraccountPermissionsKey;

    public String getReqTypePrefixKey() {
        return reqTypePrefixKey;
    }

    public String getInboundServiceKey() {
        return inboundServiceKey;
    }

    public void setInboundServiceKey(String inboundServiceKey) {
        this.inboundServiceKey = inboundServiceKey;
    }

    public String getSwiftServiceKey() {
        return swiftServiceKey;
    }

    public void setSwiftServiceKey(String swiftServiceKey) {
        this.swiftServiceKey = swiftServiceKey;
    }

    public String getFileTypeKey() {
        return fileTypeKey;
    }

    public String getMqPrefixKey() {
        return mqPrefixKey;
    }

    public String getGplUrl() {
        return gplUrl;
    }

    public String getBfgUiUrl() {
        return bfgUiUrl;
    }

    public String getCaCertUrl() {
        return caCertUrl;
    }

    public String getSysCertUrl() {
        return sysCertUrl;
    }

    public String getCertIdKey() {
        return certIdKey;
    }

    public String getFileSearchPrefixKey() {
        return fileSearchPrefixKey;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileStatusPrefixKey() {
        return fileStatusPrefixKey;
    }

    public void setReqTypePrefixKey(String reqTypePrefixKey) {
        this.reqTypePrefixKey = reqTypePrefixKey;
    }

    public void setFileTypeKey(String fileTypeKey) {
        this.fileTypeKey = fileTypeKey;
    }

    public void setMqPrefixKey(String mqPrefixKey) {
        this.mqPrefixKey = mqPrefixKey;
    }

    public void setGplUrl(String gplUrl) {
        this.gplUrl = gplUrl;
    }

    public void setBfgUiUrl(String bfgUiUrl) {
        this.bfgUiUrl = bfgUiUrl;
    }

    public void setCaCertUrl(String caCertUrl) {
        this.caCertUrl = caCertUrl;
    }

    public void setSysCertUrl(String sysCertUrl) {
        this.sysCertUrl = sysCertUrl;
    }

    public void setCertIdKey(String certIdKey) {
        this.certIdKey = certIdKey;
    }

    public void setFileSearchPrefixKey(String fileSearchPrefixKey) {
        this.fileSearchPrefixKey = fileSearchPrefixKey;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileStatusPrefixKey(String fileStatusPrefixKey) {
        this.fileStatusPrefixKey = fileStatusPrefixKey;
    }

    public String[] getFileSearchPostfixKey() {
        return fileSearchPostfixKey;
    }

    public void setFileSearchPostfixKey(String[] fileSearchPostfixKey) {
        this.fileSearchPostfixKey = fileSearchPostfixKey;
    }

    public String getFileErrorUrl() {
        return fileErrorUrl;
    }

    public void setFileErrorUrl(String fileErrorUrl) {
        this.fileErrorUrl = fileErrorUrl;
    }

    public String[] getFileErrorPostfixKey() {
        return fileErrorPostfixKey;
    }

    public void setFileErrorPostfixKey(String[] fileErrorPostfixKey) {
        this.fileErrorPostfixKey = fileErrorPostfixKey;
    }

    public String getTransactionSearchPrefixKey() {
        return transactionSearchPrefixKey;
    }

    public void setTransactionSearchPrefixKey(String transactionSearchPrefixKey) {
        this.transactionSearchPrefixKey = transactionSearchPrefixKey;
    }

    public String[] getTransactionSearchPostfixKey() {
        return transactionSearchPostfixKey;
    }

    public void setTransactionSearchPostfixKey(String[] transactionSearchPostfixKey) {
        this.transactionSearchPostfixKey = transactionSearchPostfixKey;
    }

    public String getTransactionStatusPrefixKey() {
        return transactionStatusPrefixKey;
    }

    public void setTransactionStatusPrefixKey(String transactionStatusPrefixKey) {
        this.transactionStatusPrefixKey = transactionStatusPrefixKey;
    }

    public String getUseraccountGroupsKey() {
        return useraccountGroupsKey;
    }

    public void setUseraccountGroupsKey(String useraccountGroupsKey) {
        this.useraccountGroupsKey = useraccountGroupsKey;
    }

    public String getUseraccountPermissionsKey() {
        return useraccountPermissionsKey;
    }

    public void setUseraccountPermissionsKey(String useraccountPermissionsKey) {
        this.useraccountPermissionsKey = useraccountPermissionsKey;
    }
}
