package com.ibm.sterling.bfg.app.model;

import com.ibm.sterling.bfg.app.change.model.ChangeControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Optional;

public class EntityAction implements Serializable, ByteEntity {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(EntityAction.class);
    protected Entity entity;

    public EntityAction(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    //    protected String getChanger() throws Exception{
//        String userName="FBSystem";
//        ChangeControl cc = new ChangeControl(entity.getChangeID());
//        if(cc!=null){
//            userName = cc.getChanger();
//        }
//        return userName;
//    }

    @Override
    public String toString() {
        return "EntityAction{" +
                "entity=" + entity +
                '}';
    }

//    public static Optional<EntityAction> getEntityObject(byte[] byteActionObject) {
//        Optional<EntityAction> optionalEntityAction = Optional.empty();
//        try {
//            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteActionObject));
//            EntityAction action = (EntityAction) in.readObject();
//            optionalEntityAction = Optional.ofNullable(action);
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return optionalEntityAction;
//    }

    public String getClassName() {
        return this.getClass().getName();
    }

//    public byte[] getObjectBytes() {
//        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oout = new ObjectOutputStream(baos)) {
//            oout.writeObject(this);
//            return baos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
