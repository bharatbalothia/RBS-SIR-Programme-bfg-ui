package com.ibm.sterling.bfg.app.model.certificate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sterling.bfg.app.service.certificate.IntegratedCertNameAndDate;

import java.security.cert.X509Certificate;

public class ImportedTrustedCertificateDetails extends TrustedCertificateDetails
            implements Comparable<ImportedTrustedCertificateDetails> {
    @JsonIgnore
    private X509Certificate certificate;
    private IntegratedCertNameAndDate certNameAndDate;
    private boolean isLatest;

    public ImportedTrustedCertificateDetails() {
    }

    public ImportedTrustedCertificateDetails(TrustedCertificateDetails trustedCertificateDetails) {
        super(trustedCertificateDetails);
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public IntegratedCertNameAndDate getCertNameAndDate() {
        return certNameAndDate;
    }

    public void setCertNameAndDate(IntegratedCertNameAndDate certNameAndDate) {
        this.certNameAndDate = certNameAndDate;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public void setLatest(boolean latest) {
        isLatest = latest;
    }

    @Override
    public int compareTo(ImportedTrustedCertificateDetails o) {
        return o.certNameAndDate.getCreationOrUpdateTime().compareTo(this.certNameAndDate.getCreationOrUpdateTime());
    }

    @Override
    public String toString() {
        return "ImportedTrustedCertificateDetails{" +
                "certName=" + certNameAndDate.getCertName() +
                ", certDate=" + certNameAndDate.getCreationOrUpdateTime() +
                ", isValid=" + isValid() +
                ", isLatest=" + isLatest() +
                '}';
    }

    @Override
    public TrustedCertificate convertToTrustedCertificate() {
        TrustedCertificate trustedCertificate = super.convertToTrustedCertificate();
        trustedCertificate.setCertificateName(certNameAndDate.getCertName());
        trustedCertificate.setCertificate(certificate);
        return trustedCertificate;
    }
}
