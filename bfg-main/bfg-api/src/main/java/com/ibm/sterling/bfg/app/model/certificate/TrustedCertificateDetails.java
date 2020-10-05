package com.ibm.sterling.bfg.app.model.certificate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
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
    private List<Map<String, String>> authChainReport = null;
    private boolean isValid = true;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, List<String>>> certificateErrors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, Object>> certificateWarnings;

    public TrustedCertificateDetails(X509Certificate x509Certificate, CertificateValidationService certificateValidationService,
                                     TrustedCertificateRepository trustedCertificateRepository, ChangeControlCertRepository changeControlCertRepository,
                                     boolean isCheckBeforeApproval)
            throws NoSuchAlgorithmException, CertificateEncodingException, InvalidNameException, JsonProcessingException {
        List<Map<String, List<String>>> errors = new ArrayList<>();
        Map<String, Object> warnings = new HashMap<>();
        this.serialNumber = String.valueOf(x509Certificate.getSerialNumber().intValue());
        byte[] encodedCert = x509Certificate.getEncoded();
        this.thumbprint = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-1").digest(encodedCert));
        this.thumbprint256 = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-256").digest(encodedCert));
        if (!isCheckBeforeApproval)
            checkThumbprintUniquenessLocally(trustedCertificateRepository, changeControlCertRepository, errors);
        SimpleDateFormat certificateDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.startDate = certificateDateFormat.format(x509Certificate.getNotBefore());
        this.endDate = certificateDateFormat.format(x509Certificate.getNotAfter());
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
        validateCertificateUsingAugmentedService(certificateValidationService, x509Certificate, errors, warnings);
        if (!errors.isEmpty()) {
            this.certificateErrors = errors;
            this.isValid = false;
        }
        if (!warnings.isEmpty()) {
            this.certificateWarnings = Collections.singletonList(warnings);
        }
    }

    private void validateCertificateUsingAugmentedService(CertificateValidationService certificateValidationService, X509Certificate x509Certificate,
                                                          List<Map<String, List<String>>> errors, Map<String, Object> warnings) throws JsonProcessingException, CertificateEncodingException {
        Map<String, Object> certificateChain = certificateValidationService.getCertificateChain(DatatypeConverter.printBase64Binary(x509Certificate.getEncoded()));
        Object attribute = certificateChain.get("attribute");
        String errorMessage = Optional.ofNullable(attribute)
                .map(attributeValue -> (String) Optional.ofNullable(certificateChain.get("error"))
                        .orElseGet(() -> Optional.ofNullable(certificateChain.get("message")).orElse(null))).orElse(null);
        if (errorMessage != null)
            if ("certificate.expired".equals(attribute))
                warnings.put("certificate.expired", "Certificate has expired");
            else errors.add(Collections.singletonMap(attribute.toString(), Collections.singletonList(errorMessage)));
        else if (Boolean.valueOf(Optional.ofNullable(certificateChain.get("selfSigned")).orElse("").toString()))
            warnings.put("authChainReport", "Certificate is valid but is self-signed and therefore cannot be trusted via a certificate chain");
        else this.authChainReport = new ObjectMapper().convertValue(certificateChain.get("chain"), List.class);
    }

    private void checkThumbprintUniquenessLocally(TrustedCertificateRepository trustedCertificateRepository,
                                                  ChangeControlCertRepository changeControlCertRepository, List<Map<String, List<String>>> errors) {
        if (trustedCertificateRepository.existsByThumbprint(thumbprint))
            errors.add(Collections.singletonMap("thumbprint", new ArrayList<>(Collections.singletonList("SHA-1 Thumbprint is not unique"))));
        if (trustedCertificateRepository.existsByThumbprint256(thumbprint256))
            errors.add(Collections.singletonMap("thumbprint256", Collections.singletonList("SHA-2 Thumbprint is not unique")));
        Optional<Map<String, List<String>>> listThumbprint = errors.stream().filter(error -> error.containsKey("thumbprint"))
                .findFirst();
        if (changeControlCertRepository.existsByResultMeta2AndStatus(this.thumbprint, ChangeControlStatus.PENDING))
            if (listThumbprint.isPresent())
                listThumbprint.get().get("thumbprint").add("A Trusted certificate with this SHA-1 Thumbprint is pending approval for update/insert");
            else errors.add(Collections.singletonMap("thumbprint", new ArrayList<>(
                    Collections.singletonList("A Trusted certificate with this SHA-1 Thumbprint is pending approval for update/insert"))));
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
        certificate.setThumbprint(this.thumbprint);
        certificate.setThumbprint256(this.thumbprint256);
        certificate.setStartDate(this.startDate);
        certificate.setEndDate(this.endDate);
        certificate.setIssuer(this.issuer);
        certificate.setSubject(this.subject);
        certificate.setAuthChainReport(this.authChainReport);
        return certificate;
    }

    public List<Map<String, List<String>>> getCertificateErrors() {
        return certificateErrors;
    }

    public List<Map<String, Object>> getCertificateWarnings() {
        return certificateWarnings;
    }

}
