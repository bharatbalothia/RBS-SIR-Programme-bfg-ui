package com.ibm.sterling.bfg.app.model.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.service.CertificateValidationService;
import org.springframework.security.crypto.codec.Hex;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TrustedCertificateDetails {

    private String serialNumber;
    private String thumbprint;
    private String startDate;
    private String endDate;
    private Map<String, List<String>> issuer;
    private Map<String, List<String>> subject;
    private List<Map<String, String>> authChainReport;
    private boolean isValid;

    public TrustedCertificateDetails(X509Certificate x509Certificate, CertificateValidationService certificateValidationService)
            throws NoSuchAlgorithmException, CertificateEncodingException, InvalidNameException, JsonProcessingException {
        this.serialNumber = x509Certificate.getSerialNumber().toString();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(x509Certificate.getEncoded());
        this.thumbprint = new String(Hex.encode(messageDigest.digest()));
        this.startDate = new SimpleDateFormat("dd/MM/yyyy").format(x509Certificate.getNotBefore());
        this.endDate = new SimpleDateFormat("dd/MM/yyyy").format(x509Certificate.getNotAfter());
        this.issuer = new LdapName(x509Certificate.getIssuerDN().getName())
                .getRdns().stream()
                .flatMap(rdn -> Collections.singletonMap(rdn.getType(), String.valueOf(rdn.getValue()))
                        .entrySet().stream())
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                LinkedHashMap::new,
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                        )
                );
        this.subject = new LdapName(x509Certificate.getSubjectDN().getName())
                .getRdns().stream()
                .flatMap(rdn -> Collections.singletonMap(rdn.getType(), String.valueOf(rdn.getValue()))
                        .entrySet().stream())
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList()))
                );
        List<String> rdnKeys = new ArrayList<>(issuer.keySet());
        Collections.reverse(rdnKeys);
        this.authChainReport = certificateValidationService.getCertificateChain(
                Base64.getEncoder().encodeToString(
                        rdnKeys.stream()
                                .map(constValue -> constValue + "=" +
                                        issuer.get(constValue).stream()
                                                .map(issuerValueByKey -> issuerValueByKey.replace(",", "\\"))
                                                .reduce("", (issuerValueByKeyOne, issuerValueByKeyTwo) ->
                                                        issuerValueByKeyOne + issuerValueByKeyTwo
                                                ))
                                .reduce("", (issuerValueOne, issuerValueTwo) -> issuerValueOne + issuerValueTwo)
                                .getBytes())
        );
        this.isValid = true;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getThumbprint() {
        return thumbprint;
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

    public boolean isValid() {
        return isValid;
    }

    public List<Map<String, String>> getAuthChainReport() {
        return authChainReport;
    }

    public TrustedCertificate convertToTrustedCertificate() {
        TrustedCertificate certificate = new TrustedCertificate();
        certificate.setSerialNumber(this.serialNumber);
        certificate.setCertificateThumbprint(this.thumbprint);
        certificate.setStartDate(this.startDate);
        certificate.setEndDate(this.endDate);
        certificate.setIssuer(this.issuer);
        certificate.setSubject(this.subject);
        certificate.setAuthChainReport(this.authChainReport);
        return certificate;
    }

}
