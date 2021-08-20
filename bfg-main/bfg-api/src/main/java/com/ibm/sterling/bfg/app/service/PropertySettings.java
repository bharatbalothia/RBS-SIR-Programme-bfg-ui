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
    private String transactionDirectionPrefixKey;
    private String transactionTypeKey;
    private String useraccountGroupsKey;
    private String useraccountPermissionsKey;
    private String loginText;
    private String trustedCertsImportSchedule;
    private String sepaDashboardTrxMaxValue;
    private String sepaDashboardFileMaxValue;
    private String sepaDashboardVisibility;
    private String linkF5;

    public String getReqTypePrefixKey() {
        return reqTypePrefixKey;
    }

    public String getInboundServiceKey() {
        return inboundServiceKey;
    }

    public String getSwiftServiceKey() {
        return swiftServiceKey;
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

    public String[] getFileSearchPostfixKey() {
        return fileSearchPostfixKey;
    }

    public String getFileErrorUrl() {
        return fileErrorUrl;
    }

    public String[] getFileErrorPostfixKey() {
        return fileErrorPostfixKey;
    }

    public String getTransactionStatusPrefixKey() {
        return transactionStatusPrefixKey;
    }

    public String getTransactionDirectionPrefixKey() {
        return transactionDirectionPrefixKey;
    }

    public String getTransactionTypeKey() {
        return transactionTypeKey;
    }

    public String getUseraccountGroupsKey() {
        return useraccountGroupsKey;
    }

    public String getUseraccountPermissionsKey() {
        return useraccountPermissionsKey;
    }

    public String getLoginText() {
        return loginText;
    }

    public String getTrustedCertsImportSchedule() {
        return trustedCertsImportSchedule;
    }

    public String getSepaDashboardTrxMaxValue() {
        return sepaDashboardTrxMaxValue;
    }

    public String getSepaDashboardFileMaxValue() {
        return sepaDashboardFileMaxValue;
    }

    public String getSepaDashboardVisibility() {
        return sepaDashboardVisibility;
    }

    public String getLinkF5() {
        return linkF5;
    }

    public void setReqTypePrefixKey(String reqTypePrefixKey) {
        this.reqTypePrefixKey = reqTypePrefixKey;
    }

    public void setInboundServiceKey(String inboundServiceKey) {
        this.inboundServiceKey = inboundServiceKey;
    }

    public void setSwiftServiceKey(String swiftServiceKey) {
        this.swiftServiceKey = swiftServiceKey;
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

    public void setTransactionStatusPrefixKey(String transactionStatusPrefixKey) {
        this.transactionStatusPrefixKey = transactionStatusPrefixKey;
    }

    public void setFileSearchPostfixKey(String[] fileSearchPostfixKey) {
        this.fileSearchPostfixKey = fileSearchPostfixKey;
    }

    public void setFileErrorUrl(String fileErrorUrl) {
        this.fileErrorUrl = fileErrorUrl;
    }

    public void setFileErrorPostfixKey(String[] fileErrorPostfixKey) {
        this.fileErrorPostfixKey = fileErrorPostfixKey;
    }

    public void setTransactionDirectionPrefixKey(String transactionDirectionPrefixKey) {
        this.transactionDirectionPrefixKey = transactionDirectionPrefixKey;
    }

    public void setTransactionTypeKey(String transactionTypeKey) {
        this.transactionTypeKey = transactionTypeKey;
    }

    public void setUseraccountGroupsKey(String useraccountGroupsKey) {
        this.useraccountGroupsKey = useraccountGroupsKey;
    }

    public void setUseraccountPermissionsKey(String useraccountPermissionsKey) {
        this.useraccountPermissionsKey = useraccountPermissionsKey;
    }

    public void setLoginText(String loginText) {
        this.loginText = loginText;
    }

    public void setTrustedCertsImportSchedule(String trustedCertsImportSchedule) {
        this.trustedCertsImportSchedule = trustedCertsImportSchedule;
    }

    public void setSepaDashboardTrxMaxValue(String sepaDashboardTrxMaxValue) {
        this.sepaDashboardTrxMaxValue = sepaDashboardTrxMaxValue;
    }

    public void setSepaDashboardFileMaxValue(String sepaDashboardFileMaxValue) {
        this.sepaDashboardFileMaxValue = sepaDashboardFileMaxValue;
    }

    public void setSepaDashboardVisibility(String sepaDashboardVisibility) {
        this.sepaDashboardVisibility = sepaDashboardVisibility;
    }

    public void setLinkF5(String linkF5) {
        this.linkF5 = linkF5;
    }
}
