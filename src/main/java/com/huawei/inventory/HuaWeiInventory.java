package com.huawei.inventory;

import java.util.*;

import wjl.util.YamlLoader;

/**
 * 从文件加载物理网元和链路
 */
public class HuaWeiInventory {
    public static synchronized List<String> loadFromFile(String filename) {
        List<String> errors = new ArrayList<>();
        Map<?,?> data = YamlLoader.fileToObject(Map.class, filename);
        if (data == null) {
            errors.add("Error : can't open file, or it is not a YAML file.");
            return errors;
        }

        List<PhyLink> links = parseLinks(data.get("links"), errors);
        if (links == null || !errors.isEmpty()) {
            return errors;
        }

        Set<String> devices = new HashSet<>();
        for (PhyLink lk : links) {
            PhyLinkMgr.addLink(lk);
            devices.add(lk.getSrcDevice());
            devices.add(lk.getDstDevice());
        }

        for (String dev : devices) {
            if (!PhyRouterMgr.isDeviceExist(dev)) {
                PhyRouter pr = new PhyRouter();
                pr.setName(dev);
                PhyRouterMgr.addRouter(pr);
            }
        }

        return null;
    }

    private static List<PhyLink> parseLinks(Object links, List<String> errors) {
        if (links == null) {
            errors.add("Error : no link is defined.");
            return null;
        }

        if (!(links instanceof List)) {
            errors.add("Error : links must be list.");
            return null;
        }

        List<PhyLink> ret = new ArrayList<>();
        for (Object eachLink : (List<?>)links) {
            if (!(eachLink instanceof Map)) {
                continue;
            }

            Map<?,?> objLink = (Map<?,?>)eachLink;
            Object id = objLink.get("id");
            Object srcDevice = objLink.get("srcDevice");
            Object dstDevice = objLink.get("dstDevice");
            Object srcPort = objLink.get("srcPort");
            Object dstPort = objLink.get("dstPort");

            if (id instanceof String && srcDevice instanceof String && srcPort instanceof String
                && dstDevice instanceof String && dstPort instanceof String) {
                PhyLink pl = new PhyLink();
                pl.setId((String)id);
                pl.setSrcDevice((String)srcDevice);
                pl.setSrcPort((String)srcPort);
                pl.setDstDevice((String)dstDevice);
                pl.setDstPort((String)dstPort);
                ret.add(pl);
            }
        }

        return ret;
    }
}
