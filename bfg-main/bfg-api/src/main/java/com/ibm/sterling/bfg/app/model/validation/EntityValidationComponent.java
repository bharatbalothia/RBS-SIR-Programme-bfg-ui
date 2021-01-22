package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.exception.entity.FieldsValidationException;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.validation.gplvalidation.GplValidation;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.SctValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

import static com.ibm.sterling.bfg.app.model.changecontrol.Operation.CREATE;
import static com.ibm.sterling.bfg.app.model.changecontrol.Operation.UPDATE;

@Component
public class EntityValidationComponent {

    @Autowired
    private Validator validator;

    private Class getEntityValidationGroup(Entity entity, Operation operation) {
        Map<String, Map<Operation, Class>> entityOperationMap = new HashMap<String, Map<Operation, Class>>() {
            {
                put("GPL", new HashMap<Operation, Class>() {
                            {
                                put(CREATE, GplValidation.PostValidation.class);
                                put(UPDATE, GplValidation.PutValidation.class);

                            }
                        }
                );
                put("SCT", new HashMap<Operation, Class>() {
                            {
                                put(CREATE, SctValidation.PostValidation.class);
                                put(UPDATE, SctValidation.PutValidation.class);
                            }
                        }
                );
            }
        };
        return Optional.ofNullable(entityOperationMap.get(entity.getService()))
                .map(service -> service.get(operation))
                .orElseThrow(() -> new FieldsValidationException("service", "The " + entity.getService() + " service is not allowed"));
    }

    public void validateEntity(Entity entity, Operation operation) {
        setNonServiceFieldsToDefaultValues(entity);
        Set<ConstraintViolation<Entity>> violations = validator.validate(entity, getEntityValidationGroup(entity, operation));
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void setNonServiceFieldsToDefaultValues(Entity entity) {
        String service = entity.getService();
        if ("GPL".equals(service)) {
            entity.setMaxBulksPerFile(0);
            entity.setMaxTransfersPerBulk(0);
            entity.setStartOfDay("00:00");
            entity.setEndOfDay("00:00");
            entity.setMailboxPathIn(entity.getEntity() + "_GPL");
            entity.setMailboxPathOut(entity.getEntity() + "_GPL");
            entity.setMqQueueIn(entity.getEntity() + "_GPL");
            entity.setMqQueueOut(entity.getEntity() + "_GPL");
            entity.setCompression(Boolean.FALSE);
            entity.setEntityParticipantType(null);
            entity.setDirectParticipant(null);
            entity.setSchedules(new ArrayList<>());
            entity.setMqHost(null);
            entity.setMqPort(null);
            entity.setMqQManager(null);
            entity.setMqChannel(null);
            entity.setMqQueueName(null);
            entity.setMqQueueBinding(null);
            entity.setMqQueueContext(null);
            entity.setMqDebug(null);
            entity.setMqSSLOptions(null);
            entity.setMqSSLCiphers(null);
            entity.setMqSSLKeyCert(null);
            entity.setMqSSLCaCert(null);
            entity.setMqHeader(null);
            entity.setMqSessionTimeout(null);
        } else if ("SCT".equals(service)) {
            entity.setRouteInbound(Boolean.FALSE);
            entity.setInboundRequestorDN("");
            entity.setInboundResponderDN("");
            entity.setInboundService("");
            entity.setInboundRequestType(new ArrayList<>());
            entity.setNonRepudiation(Boolean.FALSE);
            entity.setE2eSigning(null);
        } else return;
        entity.setCdNode(null);
        entity.setIdfWTOMsgId(null);
        entity.setDnfWTOMsgId(null);
        entity.setDvfWTOMsgId(null);
        entity.setSdfWTOMsgId(null);
        entity.setRsfWTOMsgId(null);
        entity.setCdfWTOMsgId(null);
        entity.setMsrWTOMsgId(null);
        entity.setPsrWTOMsgId(null);
        entity.setDrrWTOMsgId(null);
        entity.setRtfWTOMsgId(null);
        entity.setMbpWTOMsgId(null);
        entity.setIrishStep2(Boolean.FALSE);
        entity.setPauseInbound(Boolean.FALSE);
        entity.setPauseOutbound(Boolean.FALSE);
        entity.setRouteOutbound(Boolean.TRUE);
    }

}
