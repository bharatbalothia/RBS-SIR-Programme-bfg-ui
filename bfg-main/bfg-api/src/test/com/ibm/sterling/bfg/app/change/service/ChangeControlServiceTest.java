package com.ibm.sterling.bfg.app.change.service;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import com.ibm.sterling.bfg.app.change.model.ChangeControlConstants;
import com.ibm.sterling.bfg.app.change.model.ChangeControlStatus;
import com.ibm.sterling.bfg.app.change.model.Operation;
import com.ibm.sterling.bfg.app.change.repository.ChangeControlRepository;
import com.ibm.sterling.bfg.app.model.ByteEntity;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityAction;
import com.ibm.sterling.bfg.app.model.EntityFields;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChangeControlServiceTest {
    @Autowired
    private ChangeControlRepository repository;

    @Autowired
    private ChangeControlService service;

//    @Autowired
//    ChangeControlService service;

    @Test
    void listAll() {
    }

    @Test
    void findById() {
        Optional<ChangeControl> optionalChangeControl = repository.findById("ID_1000019");
        if (optionalChangeControl.isPresent()) {
            ChangeControl control = optionalChangeControl.get();
            byte[] bActionObj = control.getActionObject();
            EntityAction entityAction = (EntityAction) ByteEntity.getEntityObject(bActionObj).get();
            System.out.println((Entity) ByteEntity.getEntityObject(control.getResultObject()).get());
            System.out.println(entityAction);
        }
    }

    @Test
    void save() {
        try {
            Entity entity = new Entity();
            entity.setEntity("FOMGC2LXXX");

            ChangeControl cc = new ChangeControl();
            cc.setOperation(Operation.CREATE);
            cc.setChanger("testChanger");
            cc.setStatus(ChangeControlStatus.PENDING);
            cc.setActionObject(new EntityAction(entity).getObjectBytes());
            cc.setResultMeta1("FOMGC2LXXX");
            cc.setObjectType(ChangeControlConstants.OBJECT_TYPE);
//            cc.setDateChanged(String.valueOf("sysdate"));
//            cc.setDateChanged(String.valueOf("27-APR-2014"));
            repository.save(cc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void isNameUnique() {
        System.out.println(service.isNameUnique("FOMGB2BXXX"));
    }
}