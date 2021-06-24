package com.huawei.common;

import wjl.docker.AbstractMember;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InterfaceMgr extends AbstractMember {
    private final Map<String, Interface> ifs = new ConcurrentHashMap<>();

    public void addInterface(Interface inf) {
        ifs.put(inf.getId(), inf);
    }

    public Interface getInterface(String id) {
        return ifs.get(id);
    }

    public Interface delInterface(String id) {
        return ifs.remove(id);
    }
}
