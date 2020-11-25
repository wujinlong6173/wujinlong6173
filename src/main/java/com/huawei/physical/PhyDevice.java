package com.huawei.physical;

import wjl.cli.ConfigHolder;

/**
 * 物理路由器、物理交换机的基类，名称和标识都是唯一的。
 */
public class PhyDevice extends ConfigHolder {
    // 设备在网络意图中的标识
    private String idInNms;
    // 设备在运营商中的标识
    private String name;

    public String getIdInNms() {
        return idInNms;
    }

    public void setIdInNms(String idInNms) {
        this.idInNms = idInNms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
