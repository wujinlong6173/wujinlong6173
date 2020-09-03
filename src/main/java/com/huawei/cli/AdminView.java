package com.huawei.cli;

import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.List;

/**
 * 管理物理网络
 */
public class AdminView implements CommandView {
    @Override
    public String getPrompt() {
        return "system";
    }

    @Command(command="router")
    public List<String> listRouters() {
        return null;
    }

    @Command(command="router {name}")
    public RouterView getRouter(String name) {
        return new RouterView();
    }
}
