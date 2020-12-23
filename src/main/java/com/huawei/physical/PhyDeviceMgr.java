package com.huawei.physical;

import wjl.docker.AbstractMember;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有的物理路由器、物理交换机。
 */
public class PhyDeviceMgr extends AbstractMember {
    // 键值为物理路由器的名称
    private final Map<String, PhyRouter> routers = new ConcurrentHashMap<>();
    // 键值为物理交换机的名称
    private final Map<String, PhySwitch> switches = new ConcurrentHashMap<>();

    public PhyRouter getRouter(String name) {
        return routers.get(name);
    }

    public void addRouter(PhyRouter router) {
        routers.putIfAbsent(router.getName(), router);
    }

    public void delRouter(String name) {
        routers.remove(name);
    }

    public void addSwitch(PhySwitch sw) {
        switches.put(sw.getName(), sw);
    }

    public void delSwitch(String name) {
        switches.remove(name);
    }

    public PhyDevice getDevice(String name) {
        PhyDevice dev = routers.get(name);
        if (dev == null) {
            dev = switches.get(name);
        }
        return dev;
    }
}
