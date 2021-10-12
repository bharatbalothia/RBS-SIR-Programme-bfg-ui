package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.exception.changecontrol.InvalidUserForApprovalException;
import com.ibm.sterling.bfg.app.exception.changecontrol.StatusNotPendingException;
import com.ibm.sterling.bfg.app.model.audit.AdminAuditEventRequest;
import com.ibm.sterling.bfg.app.model.audit.EventType;
import com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.EntityLog;
import com.ibm.sterling.bfg.app.model.entity.SWIFTNetRoutingRuleServiceResponse;
import com.ibm.sterling.bfg.app.model.validation.EntityValidationComponent;
import com.ibm.sterling.bfg.app.model.validation.unique.EntityFieldName;
import com.ibm.sterling.bfg.app.repository.entity.EntityRepository;
import com.ibm.sterling.bfg.app.service.audit.AdminAuditService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static com.ibm.sterling.bfg.app.model.changecontrol.ChangeControlStatus.ACCEPTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityServiceImplTest {
    @InjectMocks
    private EntityService service = new EntityServiceImpl();

    @Mock
    private EntityRepository entityRepository;

    @Mock
    private ChangeControlService changeControlService;

    @Mock
    private AdminAuditService auditService;

    @Mock
    private EntityValidationComponent validationComponent;

    @Mock
    private AdminAuditService adminAuditService;

    @Mock
    private SWIFTNetRoutingRuleService swiftNetRoutingRuleService;

    private Entity entity;
    private List<Entity> entities;
    private ChangeControl pendingChangeControl = new ChangeControl();
    private ChangeControl acceptedChangeControl = new ChangeControl();
    private AdminAuditEventRequest entityAuditEvent = new AdminAuditEventRequest();
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        entity = new Entity();
        entity.setEntity("ASMEE2LY3TY");
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
        entity.setInboundRequestorDN("reqDN");
        entity.setInboundRequestorDN("respDN");
        entity.setInboundService("inbServ");
        entity.setInboundRequestType(Arrays.asList("pacs", "camt"));
        entities = Collections.singletonList(entity);
        acceptedChangeControl.setStatus(ChangeControlStatus.ACCEPTED);
        pendingChangeControl.setChangeID("TEST_0001");
        pendingChangeControl.setStatus(ChangeControlStatus.PENDING);
        pendingChangeControl.setOperation(Operation.CREATE);
        pendingChangeControl.setResultMeta1(entity.getEntity());
        pendingChangeControl.setChanger("TestChanger");
        pendingChangeControl.setEntityLog(new EntityLog(entity));
        entityAuditEvent = new AdminAuditEventRequest(
                pendingChangeControl, EventType.REQUEST_CANCELLED, pendingChangeControl.getResultMeta1());
        SecurityContextHolder.setContext(securityContext);
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
    void saveEntityToChangeControlShouldSavePendingEntity() {
        entity.setChangeID(pendingChangeControl.getChangeID());
        doNothing().when(changeControlService).checkOnPendingState(anyString(), anyString());
        doNothing().when(validationComponent).validateEntity(any(Entity.class), any(Operation.class));
        when(changeControlService.save(any(ChangeControl.class))).thenReturn(pendingChangeControl);
        doNothing().when(adminAuditService).fireAdminAuditEvent(any(AdminAuditEventRequest.class));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getName())
                .thenReturn("TESTUSER");
        assertEquals(entity, service.saveEntityToChangeControl(entity, Operation.CREATE));
    }

    @Test
    void getEntityAfterApprove() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getName())
                .thenReturn("TESTUSER");
        doNothing().when(changeControlService).setApproveInfo(
                any(ChangeControl.class), anyString(), anyString(), any(ChangeControlStatus.class));
        doNothing().when(adminAuditService).fireAdminAuditEvent(any(AdminAuditEventRequest.class));
        doNothing().when(validationComponent).validateEntity(any(Entity.class), any(Operation.class));
        SWIFTNetRoutingRuleServiceResponse rules = new SWIFTNetRoutingRuleServiceResponse();
        when(swiftNetRoutingRuleService.executeRoutingRuleOperation(
                any(Operation.class), any(Entity.class), anyString())).
                thenReturn(rules);
        when(entityRepository.save(any(Entity.class))).thenReturn(entity);
        when(changeControlService.save(any(ChangeControl.class))).thenReturn(acceptedChangeControl);
        entity.setRoutingRules(rules);
        assertEquals(entity, service.getEntityAfterApprove(pendingChangeControl, "approve", ACCEPTED));
    }

    @Test
    void getEntityAfterApproveShouldThrowStatusNotPendingException() {
        assertThrows(StatusNotPendingException.class,
                () -> service.getEntityAfterApprove(acceptedChangeControl, null, ACCEPTED));
    }

    @Test
    void getEntityAfterApproveShouldThrowInvalidUserForApprovalException() {
        pendingChangeControl.setChanger("TESTUSER");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getName())
                .thenReturn("TESTUSER");
        assertThrows(InvalidUserForApprovalException.class,
                () -> service.getEntityAfterApprove(pendingChangeControl, null, ACCEPTED));
    }

    @Test
    void findEntities() {
    }

    @Test
    void fieldValueExistsBesidesItselfReturnsTrueInCaseMQQUEUEOUT() {
        when(entityRepository.existsByMqQueueOutAndDeletedAndEntityIdNot(anyString(), anyBoolean(), anyInt()))
                .thenReturn(true);
        assertTrue(service.fieldValueExistsBesidesItself(1000, "field", EntityFieldName.MQQUEUEOUT.name()));
    }

    @Test
    void fieldValueExistsBesidesItselfReturnsTrueInCaseMAILBOXPATHOUT() {
        when(entityRepository.existsByMailboxPathOutAndDeletedAndEntityIdNot(anyString(), anyBoolean(), anyInt()))
                .thenReturn(true);
        assertTrue(service.fieldValueExistsBesidesItself(1000, "field", EntityFieldName.MAILBOXPATHOUT.name()));
    }

    @Test
    void fieldValueExistsBesidesItselfReturnsTrueInCaseENTITY_SERVICE() {
        Map<String, String> map = new HashMap<>();
        map.put("entity", entity.getEntity());
        map.put("service", entity.getService());
        when(entityRepository.existsByEntityAndServiceAndDeletedAndEntityIdNot(
                anyString(), anyString(), anyBoolean(), anyInt()))
                .thenReturn(true);
        assertTrue(service.fieldValueExistsBesidesItself(1000, map, EntityFieldName.ENTITY_SERVICE.name()));
    }

    @Test
    void findEntitiesByService() {
        when(entityRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(entities);
        assertEquals(entities, service.findEntitiesByService(entity.getService()));
    }

    @Test
    void findEntityNameForParticipantsReturnsParticipantsName() {
        Entity sctEntity = new Entity();
        entity.setEntityId(1001);
        entity.setEntity("RERERERE");
        entity.setService("SCT");
        entity.setEntityParticipantType("DIRECT");
        when(entityRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(entities);
        assertEquals(Collections.singletonList("RERERERE"),
                service.findEntityNameForParticipants(1000));
    }

    @Test
    void getEntityWithAttributesOfRoutingRulesReturnsEntity() {
        when(entityRepository.
                findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCase(
                        anyString(), anyString(), anyString(), anyBoolean()
                )).thenReturn(entities);
        assertEquals(entity, service.getEntityWithAttributesOfRoutingRules(
                "reqDN", "respDN", "inbServ",
                Collections.singletonList("camt")));
    }

    @Test
    void getEntityWithAttributesOfRoutingRulesReturnsNull() {
        when(entityRepository.
                findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCase(
                        anyString(), anyString(), anyString(), anyBoolean()
                )).thenReturn(new ArrayList<>());
        assertNull(service.getEntityWithAttributesOfRoutingRules(
                "reqDN", "respDN", "inbServ",
                Collections.singletonList("camt")));
    }

    @Test
    void getEntityWithAttributesOfRoutingRulesBesidesItselfReturnsEntity() {
        when(entityRepository.
                findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCaseAndEntityIdNot(
                        anyString(), anyString(), anyString(), anyBoolean(), anyInt()
                )).thenReturn(entities);
        assertEquals(entity, service.getEntityWithAttributesOfRoutingRulesBesidesItself(
              "reqDN", "respDN", "inbServ",
                Collections.singletonList("camt"), 1000
        ));
    }

    @Test
    void getEntityWithAttributesOfRoutingRulesBesidesItselfReturnsNull() {
        when(entityRepository.
                findByInboundRequestorDNAndInboundResponderDNAndInboundServiceAndDeletedAllIgnoreCaseAndEntityIdNot(
                        anyString(), anyString(), anyString(), anyBoolean(), anyInt()
                )).thenReturn(new ArrayList<>());
        assertNull(service.getEntityWithAttributesOfRoutingRulesBesidesItself(
                "reqDN", "respDN", "inbServ",
                Collections.singletonList("camt"), 1000
        ));
    }

    @Test
    void updatePendingEntity() {
        doNothing().when(changeControlService).updateChangeControl(any(ChangeControl.class), any(Entity.class));
        doNothing().when(adminAuditService).fireAdminAuditEvent(any(AdminAuditEventRequest.class));
        service.updatePendingEntity(pendingChangeControl, entity);
        verify(changeControlService).updateChangeControl(pendingChangeControl, entity);
    }

    @Test
    void cancelPendingEntity() {
        doNothing().when(changeControlService).deleteChangeControl(any(ChangeControl.class));
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