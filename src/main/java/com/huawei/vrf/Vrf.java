package com.huawei.vrf;

import java.util.HashMap;
import java.util.Map;

import com.huawei.common.Interface;

public class Vrf {
    private String id;
    private String name;
    private String host;
    private Map<String, Interface> bindInterfaces = new HashMap<>();
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * 绑定接口
     * 
     * @param portName 接口在虚拟路由器中的名称
     * @param inf 被绑定的接口 
     */
    public void bindInterface(String portName, Interface inf) {
        bindInterfaces.put(portName, inf);
    }

    public Map<String, Interface> getBindInterfaces() {
        return bindInterfaces;
    }
}
