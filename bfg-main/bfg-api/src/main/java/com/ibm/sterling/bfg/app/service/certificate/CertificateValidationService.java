package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class CertificateValidationService {

    @Value("${certificate.chain.url}")
    private String certificateChainUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, String>> getCertificateChain(String issuerDN) throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(certificateChainUrl)
                .queryParam("issuerdn", issuerDN);
        ResponseEntity<String> response = new RestTemplate().getForEntity(builder.toUriString(), String.class);
        JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        return objectMapper.convertValue(root, List.class);
    }

    public String getEncodedIssuerDN(Map<String, List<String>> issuer) {
        List<String> rdnKeys = new ArrayList<>(issuer.keySet());
        Collections.reverse(rdnKeys);
        return Base64.getEncoder().encodeToString(
                rdnKeys.stream()
                        .map(constValue -> constValue + "=" +
                                issuer.get(constValue).stream()
                                        .map(issuerValueByKey -> issuerValueByKey.replace(",", "\\"))
                                        .reduce("", (issuerValueByKeyOne, issuerValueByKeyTwo) ->
                                                issuerValueByKeyOne + issuerValueByKeyTwo
                                        ))
                        .reduce("", (issuerValueOne, issuerValueTwo) -> issuerValueOne + issuerValueTwo)
                        .getBytes())
                ;
    }

}
