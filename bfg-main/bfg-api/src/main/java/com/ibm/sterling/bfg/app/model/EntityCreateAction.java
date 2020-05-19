package com.ibm.sterling.bfg.app.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class EntityCreateAction extends EntityAction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(EntityAction.class);

    public EntityCreateAction(Entity entity) {
        super(entity);
    }
}
