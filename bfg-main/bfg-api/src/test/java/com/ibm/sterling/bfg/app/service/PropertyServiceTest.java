package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties(value = PropertySettings.class)
//@TestPropertySource("classpath:application-test.properties")
class PropertyServiceTest {
    @InjectMocks
    private PropertyService propertyService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PropertySettings settings;

    @Mock
    private EntityService entityService;

    @Mock
    private APIDetailsHandler apiDetailsHandler;

    @Mock
    private RestTemplate restTemplate;

    private String userName;

    private String password;
    private HttpHeaders headers;

    private String gplList = "[\n" +
            "  {\n" +
            "    \"propertyKey\": \"gpl.ui.rtm.BPAYM.PM\",\n" +
            "    \"propertyValue\": \"pacs.xxx.fisp.bpaym\",\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"propertyKey\": \"gpl.ui.rtm.DLINK.DL\",\n" +
            "    \"propertyValue\": \"pacs.xxx.fisp.pospay\",\n" +
            "    }\n" +
            "  }]";

    @BeforeEach
    void setUp() {
        userName = "username";
        password = "password";
        headers = new HttpHeaders();
        headers.setBasicAuth(userName, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getInboundRequestType() throws JsonProcessingException {
//        String reqTypePrefixKey = settings.getReqTypePrefixKey();
//        return getPropertyList(settings.getGplUrl()).stream()
//                .filter(property -> property.get(PROPERTY_KEY).startsWith(reqTypePrefixKey))
//                .flatMap(property -> {
//                            String propertyKey = property.get(PROPERTY_KEY);
//                            String typeKey = propertyKey.substring(propertyKey.indexOf(reqTypePrefixKey) +
//                                    reqTypePrefixKey.length());
//                            return Collections.singletonMap(typeKey,
//                                    property.get(PROPERTY_VALUE) + " (" + typeKey + ")").entrySet().stream();
//                        }
//                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                propertyUrl,
//                HttpMethod.GET,
//                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
//                String.class
//        );
//        JsonNode root = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
//        return objectMapper.convertValue(root, List.class);
        when(settings.getReqTypePrefixKey()).thenReturn("gpl.ui.rtm.");
        when(settings.getGplUrl()).thenReturn("http://gpl.com");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(gplList, HttpStatus.OK);
        when(apiDetailsHandler.getHttpHeaders(userName, password)).thenReturn(headers);
//        doReturn(responseEntity).when(restTemplate)
//                .exchange(
//                        "http://gpl.com", HttpMethod.GET,
//                        new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
//                        String.class);
//        when(restTemplate.exchange(
//                settings.getGplUrl(), HttpMethod.GET,
//                new HttpEntity<>(apiDetailsHandler.getHttpHeaders(userName, password)),
//                String.class)).thenReturn(responseEntity);
                when(restTemplate.exchange(
                anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity(gplList, HttpStatus.OK));
        Map<String, String> inboundRequestTypes = new HashMap<>();
        inboundRequestTypes.put("BPAYM.PM", "pacs.xxx.fisp.bpaym");
        inboundRequestTypes.put("DLINK.DL", "pacs.xxx.fisp.pospay");
        assertEquals(inboundRequestTypes, propertyService.getInboundRequestType());
    }

    @Test
    void getRestoredInboundRequestType() {
    }

    @Test
    void getInboundService() {
    }

    @Test
    void getSwiftService() {
    }

    @Test
    void getTimePicker() {
    }

    @Test
    void getTokenExpirationTime() {
    }

    @Test
    void getTrustedCertsImportSchedule() {
    }

    @Test
    void getFileType() {
    }

    @Test
    void getLoginText() {
    }

    @Test
    void getFileMaxValueForReport() {
    }

    @Test
    void getTrxMaxValueForReport() {
    }

    @Test
    void getSepaDashboardVisibility() {
    }

    @Test
    void getLinkF5() {
    }

    @Test
    void getUserAccountGroups() {
    }

    @Test
    void getUserAccountPermissions() {
    }

    @Test
    void getUserAuthorities() {
    }

    @Test
    void getFileCriteriaData() {
    }

    @Test
    void getEventCriteriaData() {
    }

    @Test
    void getEventTypesForUser() {
    }

    @Test
    void getTransactionCriteriaData() {
    }

    @Test
    void getStatusLabel() {
    }

    @Test
    void getErrorDetailsByCode() {
    }

    @Test
    void getMQDetails() {
    }
}