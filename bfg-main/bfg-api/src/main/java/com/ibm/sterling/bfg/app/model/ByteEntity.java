package com.ibm.sterling.bfg.app.model;

import java.io.*;
import java.util.Optional;

public interface ByteEntity {
    static Optional<ByteEntity> getEntityObject(byte[] byteActionObject) {
        Optional<ByteEntity> optionalEntity = Optional.empty();
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteActionObject))) {
            ByteEntity byteEntity = (ByteEntity) in.readObject();
            optionalEntity = Optional.ofNullable(byteEntity);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return optionalEntity;
    }

    default byte[] getObjectBytes() {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(baos)) {
            oout.writeObject(this);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
