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
        Set<ConstraintViolation<Entity>> violations = validator.validate(entity, getEntityValidationGroup(entity, operation));
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
