package com.huawei.inventory;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import wjl.util.YamlLoader;

/**
 * 简单粗暴的链路管理器
 */
public class LinkMgr {
    private static LinkMgr linkMgr;
    private List<PhyLink> links;
    
    public List<PhyLink> getLinks() {
        return links;
    }

    public void setLinks(List<PhyLink> links) {
        this.links = links;
    }

    private static void loadFromFile() {
        if (linkMgr == null) {
            synchronized (LinkMgr.class) {
                if (linkMgr == null) {
                    linkMgr = YamlLoader.fileToObject(LinkMgr.class, 
                            "/huawei/inventory.yaml");
                }
            }
        }
    }
    
    /**
     * 在两台设备之间寻找链路，假设所有链路都是双向的
     * 
     * @param src
     * @param dst
     * @return 链路标识
     */
    public static String findLinkBetweenDevice(String src, String dst) {
        loadFromFile();
        
        for (PhyLink lk : linkMgr.links) {
            if (StringUtils.equals(src, lk.getSrcDevice())) {
                if (StringUtils.equals(dst,  lk.getDstDevice())) {
                    return lk.getId();
                }
            } else if (StringUtils.equals(src,  lk.getDstDevice())) {
                if (StringUtils.equals(dst,  lk.getSrcDevice())) {
                    return lk.getId();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 输入链路标识，获取链路在指定网元上的端口
     * 
     * @param linkId
     * @param deviceId
     * @return 空表示查找失败
     */
    public static String getPortOfLink(String linkId, String deviceId) {
        PhyLink lk = getLink(linkId);
        if (lk == null) {
            return null;
        }
        
        if (StringUtils.equals(deviceId, lk.getSrcDevice())) {
            return lk.getSrcPort();
        } else if (StringUtils.equals(deviceId, lk.getDstDevice())) {
            return lk.getDstPort();
        } else {
            return null;
        }
    }
    
    /**
     * 从物理链路上分配VLAN ID
     * 
     * @param linkId
     * @return 分配到的VLAN ID，负一表示分配失败
     */
    public static int allocVlanId(String linkId) {
        PhyLink lk = getLink(linkId);
        if (lk == null) {
            return -1;
        }
        return lk.allocVlanId();
    }
    
    private static PhyLink getLink(String linkId) {
       loadFromFile();
        
        for (PhyLink lk : linkMgr.links) {
            if (StringUtils.equals(linkId, lk.getId())) {
                return lk;
            }
        }
        return null;
    }
}
