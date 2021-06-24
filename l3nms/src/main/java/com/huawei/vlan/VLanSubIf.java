package com.huawei.vlan;

import com.huawei.common.Interface;

/**
 * VLan子接口
 */
public class VLanSubIf extends Interface {
    private String port;
    private int vlanId;
    
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

    @Override
    public String getInterfaceName() {
        return port + "." + vlanId;
    }
}