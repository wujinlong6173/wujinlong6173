package com.huawei.inventory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import wjl.util.YamlLoader;

/**
 * 简单粗暴的链路管理器
 */
public class HuaWeiInventory {
    private static HuaWeiInventory huaWeiInventory;
    private List<PhyLink> links;
    private static Set<String> devices = new HashSet<>();
    
    public List<PhyLink> getLinks() {
        return links;
    }

    public void setLinks(List<PhyLink> links) {
        this.links = links;
    }

    public static void loadFromFile() {
        if (huaWeiInventory == null) {
            synchronized (HuaWeiInventory.class) {
                if (huaWeiInventory == null) {
                    huaWeiInventory = YamlLoader.fileToObject(HuaWeiInventory.class,
                            "/huawei/inventory.yaml");

                    if (huaWeiInventory != null && huaWeiInventory.links != null) {
                        for (PhyLink lk : huaWeiInventory.links) {
                            devices.add(lk.getSrcDevice());
                            devices.add(lk.getDstDevice());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 判断某设备是否存在
     * 
     * @param dev
     * @return
     */
    public static boolean isDeviceExist(String dev) {
        loadFromFile();
        return devices.contains(dev);
    }

    public static Set<String> getDevices() {
        return devices;
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
        
        for (PhyLink lk : huaWeiInventory.links) {
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
        
        for (PhyLink lk : huaWeiInventory.links) {
            if (StringUtils.equals(linkId, lk.getId())) {
                return lk;
            }
        }
        return null;
    }
}
