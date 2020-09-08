package com.huawei.inventory;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhyLinkMgr {
    private static final Map<String, PhyLink> LINKS = new ConcurrentHashMap<>();

    /**
     * 单元测试
     */
    public static void clear() {
        LINKS.clear();
    }

    public static void addLink(PhyLink link) {
        LINKS.putIfAbsent(link.getId(), link);
    }

    /**
     * 在两台设备之间寻找链路，假设所有链路都是双向的
     *
     * @param src 一端的物理网元名称
     * @param dst 另一端的物理网元名称
     * @return 物理链路
     */
    public static PhyLink findLinkBetweenDevice(String src, String dst) {
        for (PhyLink lk : LINKS.values()) {
            if (StringUtils.equals(src, lk.getSrcDevice())) {
                if (StringUtils.equals(dst,  lk.getDstDevice())) {
                    return lk;
                }
            } else if (StringUtils.equals(src,  lk.getDstDevice())) {
                if (StringUtils.equals(dst,  lk.getSrcDevice())) {
                    return lk;
                }
            }
        }

        return null;
    }
}
