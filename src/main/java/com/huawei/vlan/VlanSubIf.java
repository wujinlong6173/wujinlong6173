package com.huawei.vlan;

public class VlanSubIf {
    private String id;
    private String host;
    private String port;
    private int vlanId;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public int getVlanId() {
        return vlanId;
    }
    
    public void setVlanId(int vlanId) {
        this.vlanId = vlanId;
    }
}