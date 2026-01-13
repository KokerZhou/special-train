package com.dofun.safe.deviceinfo.bean;

/* loaded from: classes7.dex */
public class CpuBean {
    private String features;
    private String hardware;
    private String[] implementers;
    private String[] parts;

    public String[] getParts() {
        return this.parts;
    }

    public void setParts(String[] parts) {
        this.parts = parts;
    }

    public String[] getImplementers() {
        return this.implementers;
    }

    public void setImplementers(String[] implementers) {
        this.implementers = implementers;
    }

    public String getHardware() {
        return this.hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getFeatures() {
        return this.features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}
