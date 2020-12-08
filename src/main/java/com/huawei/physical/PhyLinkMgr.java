package com.huawei.physical;

import org.apache.commons.lang3.StringUtils;
import wjl.docker.AbstractMember;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhyLinkMgr extends AbstractMember {
    private final Map<String, PhyLink> links = new ConcurrentHashMap<>();

    public void addLink(PhyLink link) {
        links.putIfAbsent(link.getId(), link);
    }

    public void delLink(String id) {
        links.remove(id);
    }

    /**
     * 在两台设备之间寻找链路，假设所有链路都是双向的
     *
     * @param src 一端的物理网元名称
     * @param dst 另一端的物理网元名称
     * @return 物理链路
     */
    public PhyLink findLinkBetweenDevice(String src, String dst) {
        for (PhyLink lk : links.values()) {
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

    /**
     * 查找某个端口上有没有链路
     *
     * @param device 设备标识
     * @param port 端口名称
     * @return 端口上的链路或空
     */
    public PhyLink findLinkOnPort(String device, String port) {
        for (PhyLink lk : links.values()) {
            if (StringUtils.equals(device, lk.getSrcDevice())
                && StringUtils.equals(port, lk.getSrcPort())) {
                return lk;
            } else if (StringUtils.equals(device,  lk.getDstDevice())
                && StringUtils.equals(port, lk.getDstPort())) {
                return lk;
            }
        }

        return null;
    }
}
