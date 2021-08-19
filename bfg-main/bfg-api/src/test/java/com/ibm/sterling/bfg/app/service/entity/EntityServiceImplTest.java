package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.audit.EventType;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.repository.entity.EntityRepository;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EntityServiceImplTest {
    @InjectMocks
    private EntityService service = new EntityServiceImpl();

    @Mock
    private EntityRepository entityRepository;

    @Mock
    private ChangeControlService changeControlService;

    @Mock
    private AdminAuditService auditService;

    private Entity entity;
    private List<Entity> entities;
    private ChangeControl pendingChangeControl = new ChangeControl();
    private ChangeControl acceptedChangeControl = new ChangeControl();
    private AdminAuditEventRequest entityAuditEvent = new AdminAuditEventRequest();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
        entities = Collections.singletonList(entity);
        acceptedChangeControl.setStatus(ChangeControlStatus.ACCEPTED);
        pendingChangeControl.setChangeID("TEST_0001");
        pendingChangeControl.setStatus(ChangeControlStatus.PENDING);
        pendingChangeControl.setOperation(Operation.CREATE);
        pendingChangeControl.setResultMeta1(entity.getEntity());
        pendingChangeControl.setChanger("TestChanger");
        entityAuditEvent = new AdminAuditEventRequest(
                pendingChangeControl, EventType.REQUEST_CANCELLED, pendingChangeControl.getResultMeta1());

    }

    @Test
    void existsByServiceAndEntityShouldReturnTrue() {
        when(entityRepository.existsByServiceAndEntityAllIgnoreCase(anyString(), anyString()))
                .thenReturn(true);
        assertTrue(service.existsByServiceAndEntity(entity.getService(), entity.getEntity()));
    }

    @Test
    void existsByMailboxPathOutShouldReturnTrue() {
        when(entityRepository.existsByMailboxPathOut(anyString()))
                .thenReturn(true);
        assertTrue(service.existsByMailboxPathOut(entity.getMailboxPathOut()));
    }

    @Test
    void existsByMqQueueOutShouldReturnTrue() {
        when(entityRepository.existsByMqQueueOut(anyString()))
                .thenReturn(true);
        assertTrue(service.existsByMqQueueOut(entity.getMqQueueOut()));
    }

    @Test
    void listAllShouldReturnListOfEntities() {
        when(entityRepository.findAll()).thenReturn(entities);
        assertEquals(entities, service.listAll());
    }

    @Test
    void findByIdShouldReturnOptional() {
        when(entityRepository.findById(anyInt())).thenReturn(Optional.of(entity));
        assertEquals(Optional.of(entity), service.findById(anyInt()));
    }

    @Test
    void findNameByIdShouldReturnEntityName() {
        when(entityRepository.findById(anyInt())).thenReturn(Optional.of(entity));
        assertEquals(entity.getEntity(), service.findNameById(anyInt()));
    }

    @Test
    void save() {
    }

    @Test
    void saveEntityToChangeControl() {
    }

    @Test
    void getEntityAfterApprove() {
    }

    @Test
    void findEntities() {
    }

    @Test
    void fieldValueExists() {
    }

    @Test
    void fieldValueExistsBesidesItself() {
    }

    @Test
    void findEntitiesByService() {
    }

    @Test
    void findEntityNameForParticipants() {
    }

    @Test
    void getEntityWithAttributesOfRoutingRules() {
    }

    @Test
    void getEntityWithAttributesOfRoutingRulesBesidesItself() {
    }

    @Test
    void updatePendingEntity() {
    }

    @Test
    void cancelPendingEntity() {
        doNothing().when(changeControlService).deleteChangeControl(any(ChangeControl.class));
        doNothing().when(auditService).fireAdminAuditEvent(
                any(AdminAuditEventRequest.class));
        service.cancelPendingEntity(pendingChangeControl);
        verify(changeControlService, times(1)).deleteChangeControl(pendingChangeControl);
    }


    @AfterEach
    public void teardown() {
        entity = null;
        entities = null;;
        pendingChangeControl = null;
        acceptedChangeControl = null;
        entityAuditEvent = null;
    }
}