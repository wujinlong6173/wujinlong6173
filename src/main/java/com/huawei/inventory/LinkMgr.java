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
     * 输入链路标识，获取链路在指定网元上的端口
     * 
     * @param linkId
     * @param deviceId
     * @return 空表示查找失败
     */
    public static String getPortOfLink(String linkId, String deviceId) {
        loadFromFile();
        
        for (PhyLink lk : linkMgr.links) {
            if (StringUtils.equals(linkId, lk.getId())) {
                if (StringUtils.equals(deviceId, lk.getSrcDevice())) {
                    return lk.getSrcPort();
                } else if (StringUtils.equals(deviceId, lk.getDstDevice())) {
                    return lk.getDstPort();
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
