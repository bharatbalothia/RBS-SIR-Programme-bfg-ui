package com.ibm.sterling.bfg.app.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "property")
public class PropertySettings {

    private String userName;
    private String password;
    private String reqTypePrefixKey;
    private String fileTypeKey;
    private String mqPrefixKey;
    private String gplUrl;
    private String bfgUiUrl;
    private String caCertUrl;
    private String sysCertUrl;
    private String certIdKey;
    private String fileTypePrefixListKey;
    private String fileTypePrefixKey;
    private String fileUrl;
    private String fileStatusPrefixKey;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getReqTypePrefixKey() {
        return reqTypePrefixKey;
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

    public String getFileTypePrefixListKey() {
        return fileTypePrefixListKey;
    }

    public String getFileTypePrefixKey() {
        return fileTypePrefixKey;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileStatusPrefixKey() {
        return fileStatusPrefixKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setFileTypePrefixListKey(String fileTypePrefixListKey) {
        this.fileTypePrefixListKey = fileTypePrefixListKey;
    }

    public void setFileTypePrefixKey(String fileTypePrefixKey) {
        this.fileTypePrefixKey = fileTypePrefixKey;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileStatusPrefixKey(String fileStatusPrefixKey) {
        this.fileStatusPrefixKey = fileStatusPrefixKey;
    }

}
