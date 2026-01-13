package com.dofun.safe.deviceinfo.bean;

import org.json.JSONObject;

/* loaded from: classes7.dex */
public class StorageBean {
    private String freeMemory;
    private String freeStore;
    private String memInfo;
    private int ratioMemory;
    private int ratioStore;
    private String romSize;
    private String storePath;
    private String totalMemory;
    private String totalStore;
    private String usedMemory;
    private String usedStore;

    public String getFreeStore() {
        return this.freeStore;
    }

    public void setFreeStore(String freeStore) {
        this.freeStore = freeStore;
    }

    public String getUsedStore() {
        return this.usedStore;
    }

    public void setUsedStore(String usedStore) {
        this.usedStore = usedStore;
    }

    public String getTotalStore() {
        return this.totalStore;
    }

    public void setTotalStore(String totalStore) {
        this.totalStore = totalStore;
    }

    public int getRatioStore() {
        return this.ratioStore;
    }

    public void setRatioStore(int ratioStore) {
        this.ratioStore = ratioStore;
    }

    public String getStorePath() {
        return this.storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public String getFreeMemory() {
        return this.freeMemory;
    }

    public void setFreeMemory(String freeMemory) {
        this.freeMemory = freeMemory;
    }

    public String getUsedMemory() {
        return this.usedMemory;
    }

    public void setUsedMemory(String usedMemory) {
        this.usedMemory = usedMemory;
    }

    public String getTotalMemory() {
        return this.totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    public int getRatioMemory() {
        return this.ratioMemory;
    }

    public void setRatioMemory(int ratioMemory) {
        this.ratioMemory = ratioMemory;
    }

    public String getMemInfo() {
        return this.memInfo;
    }

    public void setMemInfo(String memInfo) {
        this.memInfo = memInfo;
    }

    public String getRomSize() {
        return this.romSize;
    }

    public void setRomSize(String romSize) {
        this.romSize = romSize;
    }

    public String toString() {
        return "StorageBean{freeStore='" + this.freeStore + "', usedStore='" + this.usedStore + "', totalStore='" + this.totalStore + "', ratioStore=" + this.ratioStore + ", storePath='" + this.storePath + "', freeMemory='" + this.freeMemory + "', usedMemory='" + this.usedMemory + "', totalMemory='" + this.totalMemory + "', ratioMemory=" + this.ratioMemory + ", memInfo='" + this.memInfo + "', romSize='" + this.romSize + "'}";
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("freeStore=", this.freeStore);
            jsonObject.put("usedStore", this.usedStore);
            jsonObject.put("totalStore", this.totalStore);
            jsonObject.put("ratioStore", this.ratioStore);
            jsonObject.put("storePath", this.storePath);
            jsonObject.put("freeMemory", this.freeMemory);
            jsonObject.put("usedMemory", this.usedMemory);
            jsonObject.put("totalMemory", this.totalMemory);
            jsonObject.put("ratioMemory", this.ratioMemory);
            jsonObject.put("memInfo", this.memInfo);
            jsonObject.put("romSize", this.romSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
