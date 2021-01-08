package com.ibm.sterling.bfg.app.model.validation;

import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.validation.gplvalidation.GplValidation;
import com.ibm.sterling.bfg.app.model.validation.sctvalidation.SctValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        return entityOperationMap.get(entity.getService()).get(operation);
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
            entity.setMailboxPathIn(null);
            entity.setMailboxPathOut(null);
            entity.setMqQueueIn(null);
            entity.setMqQueueOut(null);
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
            entity.setRouteInbound(Boolean.TRUE);
            entity.setInboundRequestorDN("");
            entity.setInboundResponderDN("");
            entity.setInboundService("");
            entity.setInboundRequestType(new ArrayList<>());
            entity.setInboundDir(Boolean.FALSE);
            entity.setInboundRoutingRule(Boolean.FALSE);
            entity.setNonRepudiation(Boolean.FALSE);
            entity.setE2eSigning(null);
        }
    }

}
