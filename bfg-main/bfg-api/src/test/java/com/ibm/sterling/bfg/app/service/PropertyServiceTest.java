package com.ibm.sterling.bfg.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp() {
    }

    @Test
    void getInboundRequestType() {
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