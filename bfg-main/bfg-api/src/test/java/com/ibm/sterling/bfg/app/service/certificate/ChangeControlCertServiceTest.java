package com.ibm.sterling.bfg.app.service.certificate;

import com.ibm.sterling.bfg.app.model.validation.CertificateValidationComponent;
import com.ibm.sterling.bfg.app.repository.certificate.ChangeControlCertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeControlCertServiceTest {
    @InjectMocks
    private ChangeControlCertService service;

    @Mock
    private ChangeControlCertRepository changeControlCertRepository;

    @Mock
    private CertificateValidationComponent certificateValidation;

    @BeforeEach
    void setUp() {
    }

    @Test
    void listAll() {
    }

    @Test
    void getChangeControlCertById() {
    }

    @Test
    void save() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void setApproveInfo() {
    }

    @Test
    void findPendingChangeControlsAsc() {
    }

    @Test
    void findPendingChangeControls() {
    }

    @Test
    void updateChangeControlCert() {
    }

    @Test
    void deleteChangeControl() {
    }

    @Test
    void checkOnPendingState() {
    }
}