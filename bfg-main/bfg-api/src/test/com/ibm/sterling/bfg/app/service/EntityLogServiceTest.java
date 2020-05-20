package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.EntityLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Table;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EntityLogServiceTest {
    @Autowired
    private EntityLogService logService;

    @Autowired
    private EntityService service;

    @Test
    void save() {
        Entity entity = service.findById(1335).orElse(new Entity());
        logService.save(entity);
    }

}