package com.ibm.sterling.bfg.app.model.certificate;

public class CertificateDataIntegrationRequest {
    private String certData;
    private String certName;
    private Boolean verifyAuthChain;
    private Boolean verifyValidity;

    public CertificateDataIntegrationRequest(String certData, String certName, Boolean verifyAuthChain, Boolean verifyValidity) {
        this.certData = certData;
        this.certName = certName;
        this.verifyAuthChain = verifyAuthChain;
        this.verifyValidity = verifyValidity;
    }

    public String getCertData() {
        return certData;
    }

    public void setCertData(String certData) {
        this.certData = certData;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public Boolean getVerifyAuthChain() {
        return verifyAuthChain;
    }

    public void setVerifyAuthChain(Boolean verifyAuthChain) {
        this.verifyAuthChain = verifyAuthChain;
    }

    public Boolean getVerifyValidity() {
        return verifyValidity;
    }

    public void setVerifyValidity(Boolean verifyValidity) {
        this.verifyValidity = verifyValidity;
    }

}
