package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.exception.TrustedDigitalCertificateException;
import com.ibm.sterling.bfg.app.model.certificate.TrustedDigitalCertificateErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibm.sterling.bfg.app.utils.RestTemplatesConstants.HEADER_PREFIX;

@Service
public class TrustedDigitalCertificateService {

    private static final String DELETING_APPROVAL_ERROR = "Trusted Certificate was not deleted";
    private static final String CONTACT_MESSAGE = "Please contact application support.";

    @Value("${trustedCertificate.url}")
    private String trustedCertificateUrl;

    @Value("${file.userName}")
    private String userName;

    @Value("${file.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    public void deleteTrustedCertificate(String certName) {
            try {
                new RestTemplate().exchange(
                        trustedCertificateUrl + certName,
                        HttpMethod.DELETE,
                        new HttpEntity<>(getHttpHeaders()),
                        String.class);
            } catch (HttpStatusCodeException e) {
                Optional.ofNullable(e.getMessage()).ifPresent(errorMessage -> {
                    TrustedDigitalCertificateErrorResponse error = null;
                    Matcher matcher = Pattern.compile("\\{(\\n.*\\n.*\\n.*)}").matcher(errorMessage);
                    if (matcher.find()) {
                        try {
                            error = new ObjectMapper().readValue(matcher.group(0), new TypeReference<TrustedDigitalCertificateErrorResponse>() {
                            });
                        } catch (JsonProcessingException ex) {
                            ex.printStackTrace();
                        }
                    }
                    throw new TrustedDigitalCertificateException(e.getStatusCode(),
                            String.format("%s - %s. %s", DELETING_APPROVAL_ERROR, error.getErrorDescription().replace(".", ""), CONTACT_MESSAGE ));
                });
            }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String userCredentials = userName + ":" + password;
        headers.set(HttpHeaders.AUTHORIZATION,
                HEADER_PREFIX + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
