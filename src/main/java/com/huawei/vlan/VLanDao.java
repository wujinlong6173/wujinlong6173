package com.huawei.vlan;

import wjl.docker.AbstractMember;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有创建了的VLanLink和VLanSubIf
 */
public final class VLanDao extends AbstractMember {
    private final Map<String, VLanLink> vLanLinks = new ConcurrentHashMap<>();

    public void addVLanLink(VLanLink link) {
        vLanLinks.put(link.getId(), link);
    }

    public void delVLanLink(String id) {
        vLanLinks.remove(id);
    }

    public VLanLink getVLanLink(String id) {
        return vLanLinks.get(id);
    }
}
