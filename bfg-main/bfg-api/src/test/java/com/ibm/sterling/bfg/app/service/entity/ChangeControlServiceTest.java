package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusPendingException;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.EntityLog;
import com.ibm.sterling.bfg.app.model.validation.EntityValidationComponent;
import com.ibm.sterling.bfg.app.repository.entity.ChangeControlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.ACCEPTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ChangeControlServiceTest {
    @InjectMocks
    private ChangeControlService service;

    @Mock
    private ChangeControlRepository controlRepository;

    @Mock
    private EntityValidationComponent entityValidation;

    private ChangeControl pendingChangeControl = new ChangeControl();
    private ChangeControl acceptedChangeControl = new ChangeControl();
    private List<ChangeControl> changeControlList = new ArrayList<>();
    private Entity entity;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        entity.setEntity("ASMEE2LY3");
        entity.setService("GPL");
        entity.setMailboxPathOut("ASOUTBOX");
        entity.setMqQueueOut("TESTMQQOUT");
        entity.setSnF(true);
        entity.setMailboxPathIn("ASINBOX");
        entity.setMaxTransfersPerBulk(10);
        entity.setMaxBulksPerFile(3);
        entity.setStartOfDay("00:00");
        entity.setEndOfDay("17:00");
        entity.setNonRepudiation(true);
        entity.setPauseInbound(true);
        entity.setPauseOutbound(true);
        acceptedChangeControl.setStatus(ChangeControlStatus.ACCEPTED);
        pendingChangeControl.setChangeID("TEST_0001");
        pendingChangeControl.setStatus(ChangeControlStatus.PENDING);
        pendingChangeControl.setOperation(Operation.CREATE);
        pendingChangeControl.setChanger("TestChanger");
        pendingChangeControl.setResultMeta1(entity.getEntity());
        changeControlList.add(pendingChangeControl);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findByIdShouldReturnOptionalOfChangeControl() {
        when(controlRepository.findById(anyString())).thenReturn(Optional.of(pendingChangeControl));
        assertEquals(Optional.of(pendingChangeControl), service.findById(pendingChangeControl.getChangeID()));
    }

    @Test
    void saveShouldPersistChangeControl() {
        when(controlRepository.save(any(ChangeControl.class))).thenReturn(pendingChangeControl);
        assertEquals(pendingChangeControl, service.save(pendingChangeControl));
    }

    @Test
    void setApproveInfoShouldSetApproveInformationForChangeControl() {
        when(controlRepository.save(any(ChangeControl.class))).thenReturn(pendingChangeControl);
        service.setApproveInfo(pendingChangeControl, "APPR_USER", "approved", ACCEPTED);
        verify(controlRepository, times(1)).save(pendingChangeControl);
    }

    @Test
    void findPendingChangeControlsAscShouldReturnSortedList() {
        ChangeControl changeControl = new ChangeControl();
        changeControl.setResultMeta1("AAAAAAAAAAA");
        pendingChangeControl.setStatus(ChangeControlStatus.PENDING);
        pendingChangeControl.setOperation(Operation.CREATE);
        changeControlList.add(changeControl);
        when(controlRepository.findAll(any(Specification.class))).thenReturn(changeControlList);
        Collections.sort(changeControlList);
        assertEquals(changeControlList,
                service.findPendingChangeControlsAsc(entity.getEntity(), entity.getService(), "testDN"));
    }

    @Test
    void findPendingChangeControlsShouldReturnList() {
        when(controlRepository.findAll(any(Specification.class))).thenReturn(changeControlList);
        assertEquals(changeControlList,
                service.findPendingChangeControls(entity.getEntity(), entity.getService(), "testDN"));
    }

    @Test
    void updateChangeControlShouldUpdateChangeControl() {
        EntityLog entityLog = new EntityLog();
        entityLog.setEntityId(1000);
        pendingChangeControl.setEntityLog(entityLog);
        doNothing().when(entityValidation).validateEntity(any(Entity.class), any(Operation.class));
        when(controlRepository.save(any(ChangeControl.class))).thenReturn(pendingChangeControl);
        service.updateChangeControl(pendingChangeControl, entity);
        verify(entityValidation, times(1)).validateEntity(entity, Operation.CREATE);
        verify(controlRepository, times(1)).save(pendingChangeControl);
    }

    @Test
    void deleteChangeControlShouldDeleteChangeControl() {
        doNothing().when(controlRepository).delete(any(ChangeControl.class));
        service.deleteChangeControl(pendingChangeControl);
        verify(controlRepository, times(1)).delete(pendingChangeControl);
    }

    @Test
    void checkStatusOfChangeControlShouldThrowException() {
        assertThrows(StatusNotPendingException.class,
                () -> service.checkStatusOfChangeControl(acceptedChangeControl));
    }

    @Test
    void checkOnPendingStateShouldThrowException() {
        when(controlRepository.existsByResultMeta1AndResultMeta2AndStatus(anyString(), anyString(), any(ChangeControlStatus.class)))
                .thenReturn(true);
        assertThrows(StatusPendingException.class,
                () -> service.checkOnPendingState("TESTENTITY", "TESTSERVISE"));
    }
}