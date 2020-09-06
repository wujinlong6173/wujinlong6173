package com.huawei.cli;

import com.huawei.inventory.HuaWeiInventory;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 管理物理网络
 */
public class AdminView implements CommandView {
    @Override
    public String getPrompt() {
        return "system";
    }

    @Command(command="load {filename}")
    public String loadPhyNet(String filename) {
        HuaWeiInventory.loadFromFile();
        return "ok";
    }

    @Command(command="router")
    public List<String> listRouters() {
        List<String> ret = new ArrayList<>(HuaWeiInventory.getDevices());
        Collections.sort(ret);
        return ret;
    }

    @Command(command="router {name}")
    public RouterView getRouter(String name) {
        return new RouterView();
    }
}
