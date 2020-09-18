package com.huawei.vrf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.huawei.common.Interface;
import wjl.cli.ConfigHolder;

public class Vrf extends ConfigHolder {
    private String id;
    private String idInNms;
    private String name;
    private String host;

    // 下面两个属性，支持传统的BGP Peer配置，对用户体现为命令
    // peer {peer} as-number {as}，内部通过import/export RT实现。
    private String bgpRouterId;
    private String rtForBgpPeer;

    // 键值是接口在虚拟路由器中的名称
    private Map<String, Interface> bindInterfaces = new HashMap<>();

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getIdInNms() {
        return idInNms;
    }

    public void setIdInNms(String idInNms) {
        this.idInNms = idInNms;
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

    public String getBgpRouterId() {
        return bgpRouterId;
    }

    public void setBgpRouterId(String bgpRouterId) {
        this.bgpRouterId = bgpRouterId;
    }

    public String getRtForBgpPeer() {
        return rtForBgpPeer;
    }

    public void setRtForBgpPeer(String rtForBgpPeer) {
        this.rtForBgpPeer = rtForBgpPeer;
    }

    /**
     * 绑定接口
     * 
     * @param portName 接口在虚拟路由器中的名称
     * @param inf 被绑定的接口 
     */
    public void bindInterface(String portName, Interface inf) {
        inf.setBindService(id);
        bindInterfaces.put(portName, inf);
    }

    /**
     * 解绑定接口
     *
     * @param inf 被绑定的接口
     */
    public void unBindInterface(Interface inf) {
        Map.Entry<String,Interface> item;
        Iterator<Map.Entry<String,Interface>> it = bindInterfaces.entrySet().iterator();
        while (it.hasNext()) {
            item = it.next();
            if (inf.equals(item.getValue())) {
                it.remove();
                inf.setBindService(null);
                return;
            }
        }
    }

    public Map<String, Interface> getBindInterfaces() {
        return bindInterfaces;
    }
}
