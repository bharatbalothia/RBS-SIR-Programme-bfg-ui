package com.ibm.sterling.bfg.app.change.model;

import java.util.List;

public interface ChangeViewer {
    public abstract ChangeControl getChange();
    public abstract String getObjectType();
    public abstract void setChange(ChangeControl change);
    public abstract String getBeforeChangeLink();
    public abstract String getAfterChangeLink();
    public abstract String getDifferencesLink();
    public abstract List<DiffViewer> getDifferences();
}
