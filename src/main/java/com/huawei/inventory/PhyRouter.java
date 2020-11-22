package com.huawei.inventory;

import wjl.cli.ConfigHolder;

/**
 * 物理路由器，名称和标识都是唯一的
 */
public class PhyRouter extends ConfigHolder {
    private String idInNms;
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

    public String getAsNumber() {
        return "100";
    }
}
