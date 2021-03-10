package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.APIDetailsHandler;
import com.ibm.sterling.bfg.app.exception.certificate.CertificateApprovalException;
import com.ibm.sterling.bfg.app.model.certificate.CertificateDataIntegrationRequest;
import com.ibm.sterling.bfg.app.model.certificate.IntegratedCertificateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CertificateIntegrationService {

    private static final String CREATION_APPROVAL_ERROR = "Trusted Certificate was not created in BI";
    private static final String DELETING_APPROVAL_ERROR = "Trusted Certificate was not deleted in BI";

    @Value("${trustedDigitalCertificates.url}")
    private String trustedDigitalCertificatesUrl;

    @Value("${api.userName}")
    private String userName;

    @Value("${api.password}")
    private String password;

    @Autowired
    private APIDetailsHandler apiDetailsHandler;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, String> createCertificate(CertificateDataIntegrationRequest certificateDataIntegrationRequest)
            throws JsonProcessingException {
        String certificateIntegrationResponse;
        try {
            certificateIntegrationResponse = new RestTemplate().postForObject(
                    trustedDigitalCertificatesUrl,
                    new HttpEntity<>(certificateDataIntegrationRequest, apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class
            );
        } catch (HttpStatusCodeException e) {
            throw new CertificateApprovalException(e.getMessage(), CREATION_APPROVAL_ERROR);
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(certificateIntegrationResponse));
        return objectMapper.convertValue(root, new TypeReference<Map<String, String>>() {
        });
    }

    public IntegratedCertificateData getCertificateByName(String certificateName) throws JsonProcessingException {
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().exchange(
                    trustedDigitalCertificatesUrl + certificateName,
                    HttpMethod.GET,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
        } catch (HttpStatusCodeException e) {
            return null;
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return objectMapper.convertValue(root, new TypeReference<IntegratedCertificateData>() {
        });
    }

    public List<IntegratedCertificateData> getCertificates() throws JsonProcessingException {
        ResponseEntity<String> response;
        try {
            response = new RestTemplate().exchange(
                    trustedDigitalCertificatesUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
        } catch (HttpStatusCodeException e) {
            return Collections.emptyList();
        }
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return objectMapper.convertValue(root, new TypeReference<List<IntegratedCertificateData>>() {
        });
    }

    public void deleteCertificateByName(String certificateName) {
        try {
            new RestTemplate().exchange(
                    trustedDigitalCertificatesUrl + certificateName,
                    HttpMethod.DELETE,
                    new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
                    String.class);
        } catch (HttpStatusCodeException e) {
            throw new CertificateApprovalException(e.getMessage(), DELETING_APPROVAL_ERROR);
        }
    }

}
