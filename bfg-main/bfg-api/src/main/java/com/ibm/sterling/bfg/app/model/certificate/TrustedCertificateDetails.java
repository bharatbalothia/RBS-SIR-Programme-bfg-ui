package com.ibm.sterling.bfg.app.model.certificate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.certificate.CertificateValidationService;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.xml.bind.DatatypeConverter;
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
    private String thumbprint256;
    private String startDate;
    private String endDate;
    private Map<String, List<String>> issuer;
    private Map<String, List<String>> subject;
    private List<Map<String, String>> authChainReport;
    private boolean isValid = true;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> certificateErrors;

    public TrustedCertificateDetails(X509Certificate x509Certificate, CertificateValidationService certificateValidationService)
            throws NoSuchAlgorithmException, CertificateEncodingException, InvalidNameException, JsonProcessingException {
        Map<String, Object> errors = new HashMap<>();
        this.serialNumber = String.valueOf(x509Certificate.getSerialNumber().intValue());
        byte[] encodedCert = x509Certificate.getEncoded();
        this.thumbprint = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-1").digest(encodedCert));
        this.thumbprint256 = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-256").digest(encodedCert));
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
        List<Map<String, String>> certificateChainResponse = certificateValidationService.getCertificateChain(
                certificateValidationService.getEncodedIssuerDN(issuer));
        if (certificateChainResponse.get(0).containsKey("error")) {
            this.authChainReport = null;
            errors.put("authChainReport", new ObjectMapper().readValue(
                    certificateChainResponse.get(0).get("error"), new TypeReference<Map<String, String>>() {
                    })
            );
        } else this.authChainReport = certificateChainResponse;
        if (!errors.isEmpty()) {
            this.certificateErrors = errors;
            this.isValid = false;
        }
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
        certificate.setCertificateThumbprint256(this.thumbprint256);
        certificate.setStartDate(this.startDate);
        certificate.setEndDate(this.endDate);
        certificate.setIssuer(this.issuer);
        certificate.setSubject(this.subject);
        certificate.setAuthChainReport(this.authChainReport);
        return certificate;
    }

    public Map<String, Object> getCertificateErrors() {
        return certificateErrors;
    }

}
