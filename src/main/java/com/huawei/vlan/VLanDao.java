package com.huawei.vlan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有创建了的VLanLink和VLanSubIf
 */
public final class VLanDao {
    private static Map<String, VLanLink> vLanLinks;

    static {
        vLanLinks = new ConcurrentHashMap<>();
    }

    public static void addVLanLink(VLanLink link) {
        vLanLinks.put(link.getId(), link);
    }

    public static void delVLanLink(String id) {
        vLanLinks.remove(id);
    }

    public static VLanLink getVLanLink(String id) {
        return vLanLinks.get(id);
    }
}
