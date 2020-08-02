package com.huawei.vlan;

public class VlanSubIf {
    private String id;
    private String host;
    private String phyLink;
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
    
    public String getPhyLink() {
        return phyLink;
    }
    
    public void setPhyLink(String phyLink) {
        this.phyLink = phyLink;
    }
    
    public int getVlanId() {
        return vlanId;
    }
    
    public void setVlanId(int vlanId) {
        this.vlanId = vlanId;
    }
}