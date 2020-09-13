package com.huawei.inventory;

import wjl.cli.ConfigHolder;

/**
 * 物理路由器
 */
public class PhyRouter extends ConfigHolder {
    private String name;

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
