package com.huawei.physical;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有的物理路由器、物理交换机。
 */
public class PhyDeviceMgr {
    // 键值为物理路由器的名称
    private static final Map<String, PhyRouter> ROUTERS = new ConcurrentHashMap<>();
    // 键值为物理交换机的名称
    private static final Map<String, PhySwitch> SWITCHES = new ConcurrentHashMap<>();

    /**
     * 单元测试
     */
    public static void clear() {
        ROUTERS.clear();
        SWITCHES.clear();
    }

    public static PhyRouter getRouter(String name) {
        return ROUTERS.get(name);
    }

    public static void addRouter(PhyRouter router) {
        ROUTERS.putIfAbsent(router.getName(), router);
    }

    public static void delRouter(String name) {
        ROUTERS.remove(name);
    }

    public static void addSwitch(PhySwitch sw) {
        SWITCHES.put(sw.getName(), sw);
    }

    public static void delSwitch(String name) {
        SWITCHES.remove(name);
    }

    public static PhyDevice getDevice(String name) {
        PhyDevice dev = ROUTERS.get(name);
        if (dev == null) {
            dev = SWITCHES.get(name);
        }
        return dev;
    }

    /**
     *
     * @param idInNms 物理路由器在网络意图中的标识
     * @return
     */
    public static PhyRouter getRouterByNmsId(String idInNms) {
        for (PhyRouter pr : ROUTERS.values()) {
            if (StringUtils.equals(idInNms, pr.getIdInNms())) {
                return pr;
            }
        }
        return null;
    }

}
