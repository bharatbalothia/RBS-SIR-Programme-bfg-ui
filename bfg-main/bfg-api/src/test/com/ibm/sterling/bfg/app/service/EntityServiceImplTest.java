package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.change.repository.ChangeControlRepository;
import com.ibm.sterling.bfg.app.change.service.ChangeControlService;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityFields;
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
        entity.setEntity("FOMGC2LXXX");
        entity.setService("GPL");
        entity.setChangerComments("TEST_comments");
        service.saveEntityToChangeControl(entity);
    }

    @Test
    void save() {
        Entity entity = new Entity();
        entity.setEntity("FOMGC2LXYW");
        entity.setService("GPL");
        EntityFields entityFields = new EntityFields();
        entityFields.setSnF(true);
        entityFields.setMailboxPathIn("DEINBOX");
        entityFields.setMailboxPathOut("DEOUTBOX");
        entityFields.setMaxTransfersPerBulk(10);
        entityFields.setMaxBulksPerFile(3);
        entityFields.setStartOfDay(0);
        entityFields.setEndOfDay(0);
        entity.setEntityFields(entityFields);
        service.save(entity);
    }
}