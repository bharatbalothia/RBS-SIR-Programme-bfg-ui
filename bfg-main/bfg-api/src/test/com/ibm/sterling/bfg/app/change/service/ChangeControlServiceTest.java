package com.ibm.sterling.bfg.app.change.service;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import com.ibm.sterling.bfg.app.change.model.ChangeControlConstants;
import com.ibm.sterling.bfg.app.change.model.ChangeControlStatus;
import com.ibm.sterling.bfg.app.change.model.Operation;
import com.ibm.sterling.bfg.app.change.repository.ChangeControlRepository;
import com.ibm.sterling.bfg.app.model.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ChangeControlServiceTest {
    @Autowired
    private ChangeControlRepository repository;

    @Autowired
    private ChangeControlService service;

    @Test
    void listAll() {
    }

    @Test
    void findById() {
        Optional<ChangeControl> optionalChangeControl = repository.findById("ID_1000019");
        if (optionalChangeControl.isPresent()) {
            ChangeControl control = optionalChangeControl.get();

        }
    }

    @Test
    void save() {
        try {
            Entity entity = new Entity();
            entity.setEntity("FOMGC2LEXX");
            ChangeControl cc = new ChangeControl();
            cc.setOperation(Operation.CREATE);
            cc.setChanger("testChanger");
            cc.setStatus(ChangeControlStatus.PENDING);
            cc.setResultMeta1("FOMGC2LXXX");
            cc.setObjectType(ChangeControlConstants.OBJECT_TYPE);
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