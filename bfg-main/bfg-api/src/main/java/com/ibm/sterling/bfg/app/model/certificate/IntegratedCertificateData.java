package com.ibm.sterling.bfg.app.model.certificate;

import com.ibm.sterling.bfg.app.model.DetailFormat;

public class IntegratedCertificateData {
    private String _id;
    private String _title;
    private String href;
    private String certName;
    private String certData;
    private String systemCertId;
    private String createdOrUpdatedBy;
    private String creationOrUpdateTime;
    private DetailFormat verifyValidity;
    private DetailFormat verifyAuthChain;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertData() {
        return certData;
    }

    public void setCertData(String certData) {
        this.certData = certData;
    }

    public String getSystemCertId() {
        return systemCertId;
    }

    public void setSystemCertId(String systemCertId) {
        this.systemCertId = systemCertId;
    }

    public String getCreatedOrUpdatedBy() {
        return createdOrUpdatedBy;
    }

    public void setCreatedOrUpdatedBy(String createdOrUpdatedBy) {
        this.createdOrUpdatedBy = createdOrUpdatedBy;
    }

    public String getCreationOrUpdateTime() {
        return creationOrUpdateTime;
    }

    public void setCreationOrUpdateTime(String creationOrUpdateTime) {
        this.creationOrUpdateTime = creationOrUpdateTime;
    }

    public DetailFormat getVerifyValidity() {
        return verifyValidity;
    }

    public void setVerifyValidity(DetailFormat verifyValidity) {
        this.verifyValidity = verifyValidity;
    }

    public DetailFormat getVerifyAuthChain() {
        return verifyAuthChain;
    }

    public void setVerifyAuthChain(DetailFormat verifyAuthChain) {
        this.verifyAuthChain = verifyAuthChain;
    }

}
