package com.huawei.inventory;

/**
 * 物理链路
 */
public class PhyLink {
    private String id;
    private String srcDevice;
    private String dstDevice;
    private String srcPort;
    private String dstPort;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSrcDevice() {
        return srcDevice;
    }
    
    public void setSrcDevice(String srcDevice) {
        this.srcDevice = srcDevice;
    }
    
    public String getDstDevice() {
        return dstDevice;
    }
    
    public void setDstDevice(String dstDevice) {
        this.dstDevice = dstDevice;
    }
    
    public String getSrcPort() {
        return srcPort;
    }
    
    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }
    
    public String getDstPort() {
        return dstPort;
    }
    
    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }
}
