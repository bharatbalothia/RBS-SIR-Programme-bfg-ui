package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.change.service.ChangeControlService;
import com.ibm.sterling.bfg.app.model.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EntityServiceImplTest {
    @Autowired
    private EntityService service;

    @Autowired
    private ChangeControlService controlService;

    @Test
    void saveEntityToChangeControl() {
        Entity entity = new Entity();
        entity.setEntity("ASMEE2LY3");
        entity.setService("GPL");
        entity.setMailboxPathOut("ASOUTBOX");
        entity.setSnF(true);
        entity.setMailboxPathIn("ASINBOX");
        entity.setMaxTransfersPerBulk(10);
        entity.setMaxBulksPerFile(3);
        entity.setStartOfDay(0);
        entity.setEndOfDay(0);
        entity.setNonRepudiation(true);
        entity.setPauseInbound(true);
        entity.setPauseOutbound(true);
        service.saveEntityToChangeControl(entity);
    }

    @Test
    void getEntityAfterApprove() {
        Entity entity = null;
//        try {
//            entity = service.getEntityAfterApprove("ID_1000059", "", (Integer) approverComments.get("status"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println(entity);
    }
}