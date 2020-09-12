package com.huawei.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InterfaceMgr {
    private static Map<String, Interface> ifs;

    static {
        ifs = new ConcurrentHashMap<>();
    }

    public static void addInterface(Interface inf) {
        ifs.put(inf.getId(), inf);
    }

    public static Interface getInterface(String id) {
        return ifs.get(id);
    }

    public static Interface delInterface(String id) {
        return ifs.remove(id);
    }
}
