package com.ibm.sterling.bfg.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = PropertySettings.class)
@TestPropertySource("classpath:application-test.properties")
class PropertySettingsTest {

    @Autowired
    private PropertySettings propertySettings;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getReqTypePrefixKey_ShouldReturnValue() {
        assertEquals("gpl.ui.rtm.", propertySettings.getReqTypePrefixKey());
    }

    @Test
    void getInboundServiceKey_ShouldReturnValue() {
        assertEquals("entity.inbound.service", propertySettings.getInboundServiceKey());
    }

    @Test
    void getSwiftServiceKey_ShouldReturnValue() {
        assertEquals("entity.swift.service.default", propertySettings.getSwiftServiceKey());
    }

    @Test
    void getFileTypeKey_ShouldReturnValue() {
        assertEquals("entity.schedule.filetype", propertySettings.getFileTypeKey());
    }

    @Test
    void getMqPrefixKey_ShouldReturnValue() {
        assertEquals("entity.mq.", propertySettings.getMqPrefixKey());
    }

    @Test
    void getGplUrl_ShouldReturnValue() {
        assertEquals("http://gpl.com", propertySettings.getGplUrl());
    }

    @Test
    void getBfgUiUrl_ShouldReturnValue() {
        assertEquals("http://bfgui.com", propertySettings.getBfgUiUrl());
    }

    @Test
    void getCaCertUrl_ShouldReturnValue() {
        assertEquals("http://caCertUrl.com", propertySettings.getCaCertUrl());
    }

    @Test
    void getSysCertUrl_ShouldReturnValue() {
        assertEquals("http://sysCertUrl.com", propertySettings.getSysCertUrl());
    }

    @Test
    void getCertIdKey_ShouldReturnValue() {
        assertEquals("_id", propertySettings.getCertIdKey());
    }

    @Test
    void getFileSearchPrefixKey_ShouldReturnValue() {
        assertEquals("file.search.", propertySettings.getFileSearchPrefixKey());
    }

    @Test
    void getFileUrl_ShouldReturnValue() {
        assertEquals("http://fileUrl.com", propertySettings.getFileUrl());
    }

    @Test
    void getFileStatusPrefixKey_ShouldReturnValue() {
        assertEquals(".file.status.", propertySettings.getFileStatusPrefixKey());
    }

    @Test
    void getFileSearchPostfixKey_ShouldReturnValue() {
        assertArrayEquals(new String[]{"service", "bpstate", "direction"}, propertySettings.getFileSearchPostfixKey());
    }

    @Test
    void getFileErrorUrl_ShouldReturnValue() {
        assertEquals("http://fileErrorUrl.com", propertySettings.getFileErrorUrl());
    }

    @Test
    void getFileErrorPostfixKey_ShouldReturnValue() {
        assertArrayEquals(new String[]{"Name", "Description"}, propertySettings.getFileErrorPostfixKey());
    }

    @Test
    void getTransactionStatusPrefixKey_ShouldReturnValue() {
        assertEquals(".trx.status.", propertySettings.getTransactionStatusPrefixKey());
    }

    @Test
    void getTransactionDirectionPrefixKey_ShouldReturnValue() {
        assertEquals("transaction.search.direction.", propertySettings.getTransactionDirectionPrefixKey());
    }

    @Test
    void getTransactionTypeKey_ShouldReturnValue() {
        assertEquals("transaction.search.type", propertySettings.getTransactionTypeKey());
    }

    @Test
    void getUseraccountGroupsKey_ShouldReturnValue() {
        assertEquals("useraccount.groups", propertySettings.getUseraccountGroupsKey());
    }

    @Test
    void getUseraccountPermissionsKey_ShouldReturnValue() {
        assertEquals("useraccount.permissions", propertySettings.getUseraccountPermissionsKey());
    }

    @Test
    void getLoginText_ShouldReturnValue() {
        assertEquals("login.text", propertySettings.getLoginText());
    }

    @Test
    void getTrustedCertsImportSchedule_ShouldReturnValue() {
        assertEquals("trustedcerts.import.schedule", propertySettings.getTrustedCertsImportSchedule());
    }

    @Test
    void getSepaDashboardTrxMaxValue_ShouldReturnValue() {
        assertEquals("sepadashboard.report.trx.maxvalue", propertySettings.getSepaDashboardTrxMaxValue());
    }

    @Test
    void getSepaDashboardFileMaxValue_ShouldReturnValue() {
        assertEquals("sepadashboard.report.file.maxvalue", propertySettings.getSepaDashboardFileMaxValue());
    }

    @Test
    void getSepaDashboardVisibility_ShouldReturnValue() {
        assertEquals("sepadashboard.visibility", propertySettings.getSepaDashboardVisibility());
    }

    @Test
    void getLinkF5_ShouldReturnValue() {
        assertEquals("link.f5", propertySettings.getLinkF5());
    }

}