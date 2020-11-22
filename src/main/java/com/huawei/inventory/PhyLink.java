package com.huawei.inventory;

import org.apache.commons.lang3.StringUtils;

/**
 * 物理链路
 */
public class PhyLink {
    private String id;

    /**
     * 两端设备的名称，不是UUID，因为创建VRF时输入的是物理设备的名称。
     */
    private String srcDevice;
    private String dstDevice;
    private String srcPort;
    private String dstPort;
    private int nextVLan;
    
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
    
    /**
     * 简单粗暴地分配VLAN ID
     * 
     * @return 分配到得VLAN ID
     */
    public int allocVLanId() {
        return ++nextVLan;
    }

    /**
     * 获取链路在指定网元上的端口
     *
     * @param deviceName 物理设备的名称
     * @return 空表示查找失败
     */
    public String getPortOfLink( String deviceName) {
        if (StringUtils.equals(deviceName, srcDevice)) {
            return srcPort;
        } else if (StringUtils.equals(deviceName, dstDevice)) {
            return dstPort;
        } else {
            return null;
        }
    }
}
