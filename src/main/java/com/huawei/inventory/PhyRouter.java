package com.huawei.inventory;

import com.huawei.common.CLI;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 物理路由器
 */
public class PhyRouter {
    private String name;
    private List<String> configs = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addConfig(String... cmd) {
        configs.add(StringUtils.join(cmd, ' '));
    }

    public void undoConfig(String... cmd) {
        configs.add("undo " + StringUtils.join(cmd, ' '));
    }

    public List<String> getConfigs() {
        return configs;
    }
}
