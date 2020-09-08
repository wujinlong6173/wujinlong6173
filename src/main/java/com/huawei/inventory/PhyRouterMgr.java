package com.huawei.inventory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PhyRouterMgr {
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

    /**
     * 判断某设备是否存在
     *
     * @param dev
     * @return
     */
    public static boolean isDeviceExist(String dev) {
        return ROUTERS.containsKey(dev);
    }

    public static Set<String> getDevices() {
        return ROUTERS.keySet();
    }
}
