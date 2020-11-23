package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.model.certificate.TrustedCertificateDetails;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControlStatus;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import com.ibm.sterling.bfg.app.repository.certificate.TrustedCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class TrustedCertificateDetailsService {

    @Autowired
    private CertificateValidationService certificateValidationService;

    @Autowired
    private TrustedCertificateRepository trustedCertificateRepository;

    @Autowired
    private ChangeControlCertRepository changeControlCertRepository;

    public TrustedCertificateDetails getTrustedCertificateDetails(X509Certificate x509Certificate, boolean isCheckBeforeApproval)
            throws NoSuchAlgorithmException, CertificateEncodingException, InvalidNameException, JsonProcessingException {
        TrustedCertificateDetails trustedCertificateDetails = new TrustedCertificateDetails();
        List<Map<String, List<String>>> errors = new ArrayList<>();
        Map<String, Object> warnings = new HashMap<>();
        trustedCertificateDetails.setSerialNumber(String.valueOf(x509Certificate.getSerialNumber().intValue()));
        byte[] encodedCert = x509Certificate.getEncoded();
        trustedCertificateDetails.setThumbprint(DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-1").digest(encodedCert)));
        trustedCertificateDetails.setThumbprint256(DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-256").digest(encodedCert)));
        if (!isCheckBeforeApproval)
            checkThumbprintUniquenessLocally(trustedCertificateDetails, errors);
        SimpleDateFormat certificateDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        trustedCertificateDetails.setStartDate(certificateDateFormat.format(x509Certificate.getNotBefore()));
        trustedCertificateDetails.setEndDate(certificateDateFormat.format(x509Certificate.getNotAfter()));
        trustedCertificateDetails.setIssuer(
                new LdapName(x509Certificate.getIssuerDN().getName())
                        .getRdns().stream()
                        .flatMap(rdn -> Collections.singletonMap(rdn.getType(), String.valueOf(rdn.getValue()))
                                .entrySet().stream())
                        .collect(
                                Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        LinkedHashMap::new,
                                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                                )
                        )
        );
        trustedCertificateDetails.setSubject(
                new LdapName(x509Certificate.getSubjectDN().getName())
                        .getRdns().stream()
                        .flatMap(rdn -> Collections.singletonMap(rdn.getType(), String.valueOf(rdn.getValue()))
                                .entrySet().stream())
                        .collect(
                                Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()))
                        )
        );
        validateCertificateUsingAugmentedService(x509Certificate, trustedCertificateDetails, errors, warnings);
        if (!errors.isEmpty()) {
            trustedCertificateDetails.setCertificateErrors(errors);
            trustedCertificateDetails.setIsValid(false);
        }
        if (!warnings.isEmpty()) {
            trustedCertificateDetails.setCertificateWarnings(Collections.singletonList(warnings));
        }
        return trustedCertificateDetails;
    }

    private void validateCertificateUsingAugmentedService(X509Certificate x509Certificate, TrustedCertificateDetails trustedCertificateDetails,
                                                          List<Map<String, List<String>>> errors, Map<String, Object> warnings)
            throws JsonProcessingException, CertificateEncodingException {
        Map<String, Object> certificateChain = certificateValidationService.getCertificateChain(DatatypeConverter.printBase64Binary(x509Certificate.getEncoded()));
        String attribute = (String) certificateChain.get("attribute");
        String errorMessage = Optional.ofNullable(attribute)
                .map(attributeValue -> (String) Optional.ofNullable(certificateChain.get("error"))
                        .orElseGet(() -> Optional.ofNullable(certificateChain.get("message"))
                                .orElse(null))
                ).orElseGet(() -> (String) Optional.ofNullable(certificateChain.get("errorMessage")).orElse(null));
        if (errorMessage != null)
            if ("certificate.expired".equals(attribute))
                warnings.put("certificate.expired", "Certificate has expired");
            else
                errors.add(Collections.singletonMap(Optional.ofNullable(attribute).orElse("error"), Collections.singletonList(errorMessage)));
        else if (Boolean.valueOf(Optional.ofNullable(certificateChain.get("selfSigned")).orElse("").toString()))
            warnings.put("authChainReport", "Certificate is valid but is self-signed and therefore cannot be trusted via a certificate chain");
        else
            trustedCertificateDetails.setAuthChainReport(new ObjectMapper().convertValue(certificateChain.get("chain"), List.class));
    }

    private void checkThumbprintUniquenessLocally(TrustedCertificateDetails trustedCertificateDetails, List<Map<String, List<String>>> errors) {
        if (trustedCertificateRepository.existsByThumbprint(trustedCertificateDetails.getThumbprint()))
            errors.add(Collections.singletonMap("thumbprint", new ArrayList<>(Collections.singletonList("SHA-1 Thumbprint is not unique"))));
        if (trustedCertificateRepository.existsByThumbprint256(trustedCertificateDetails.getThumbprint256()))
            errors.add(Collections.singletonMap("thumbprint256", Collections.singletonList("SHA-2 Thumbprint is not unique")));
        Optional<Map<String, List<String>>> listThumbprint = errors.stream().filter(error -> error.containsKey("thumbprint"))
                .findFirst();
        if (changeControlCertRepository.existsByResultMeta2AndStatus(trustedCertificateDetails.getThumbprint(), ChangeControlStatus.PENDING))
            if (listThumbprint.isPresent())
                listThumbprint.get().get("thumbprint").add("A Trusted certificate with this SHA-1 Thumbprint is pending approval for update/insert");
            else errors.add(Collections.singletonMap("thumbprint", new ArrayList<>(
                    Collections.singletonList("A Trusted certificate with this SHA-1 Thumbprint is pending approval for update/insert"))));
    }

}
