package com.huawei.vlan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有创建了的VLanLink和VLanSubIf
 */
public final class VLanDao {
    private static Map<String, VLanLink> vLanLinks;
    private static Map<String, VLanSubIf> vLanSubIfs;

    static {
        vLanLinks = new ConcurrentHashMap<>();
        vLanSubIfs = new ConcurrentHashMap<>();
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

    public static void addVLanSubIf(VLanSubIf subIf) {
        vLanSubIfs.put(subIf.getId(), subIf);
    }

    public static void delVLanSubIf(String id) {
        vLanSubIfs.remove(id);
    }

    public static VLanSubIf getVLanSubIf(String id) {
        return vLanSubIfs.get(id);
    }
}
