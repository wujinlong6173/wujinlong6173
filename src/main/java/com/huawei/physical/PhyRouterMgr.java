package com.huawei.physical;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PhyRouterMgr {
    // 键值为物理路由器的名称
    private static final Map<String, PhyRouter> ROUTERS = new ConcurrentHashMap<>();

    /**
     * 单元测试
     */
    public static void clear() {
        ROUTERS.clear();
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

    /**
     * 判断某设备是否存在
     *
     * @param name 物理路由器的名称
     * @return
     */
    public static boolean isDeviceExist(String name) {
        return ROUTERS.containsKey(name);
    }

    /**
     *
     * @return 物理路由器的名称列表
     */
    public static Set<String> getDevices() {
        return ROUTERS.keySet();
    }
}
