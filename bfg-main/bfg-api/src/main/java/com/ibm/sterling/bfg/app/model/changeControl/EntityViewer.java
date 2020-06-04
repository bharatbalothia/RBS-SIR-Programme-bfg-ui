package com.ibm.sterling.bfg.app.model.changeControl;

import java.util.List;

public class EntityViewer implements ChangeViewer {
    private ChangeControl changeControl;
    public final static String OBJECT_TYPE="Entity";

    public EntityViewer(){

    }

    public EntityViewer(ChangeControl changeControl) {
        this.changeControl = changeControl;
    }

    @Override
    public ChangeControl getChange() {
        return changeControl;
    }

    @Override
    public String getObjectType() {
        return OBJECT_TYPE;
    }

    @Override
    public void setChange(ChangeControl changeControl) {
        this.changeControl = changeControl;
    }

    @Override
    public String getBeforeChangeLink() {
        return null;
    }

    @Override
    public String getAfterChangeLink() {
        return null;
    }

    @Override
    public String getDifferencesLink() {
        return null;
    }

    @Override
    public List<DiffViewer> getDifferences() {
        return null;
    }
}
