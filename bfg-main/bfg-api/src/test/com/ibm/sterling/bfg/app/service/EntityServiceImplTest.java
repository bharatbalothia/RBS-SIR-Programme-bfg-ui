package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.change.repository.ChangeControlRepository;
import com.ibm.sterling.bfg.app.change.service.ChangeControlService;
import com.ibm.sterling.bfg.app.model.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EntityServiceImplTest {
    @Autowired
    private EntityService service;

    @Test
    void saveEntityToChangeControl() {
        Entity entity = new Entity();
        entity.setEntity("FOMGC2LXX7");
        entity.setChangerComments("TEST_comments");
        service.saveEntityToChangeControl(entity);
    }
}