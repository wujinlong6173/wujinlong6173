package com.huawei.cli;

import wjl.cli.Command;

import java.util.List;

/**
 * 管理物理网络
 */
public class AdminView {
    @Command(command="router")
    public List<String> listRouters() {
        return null;
    }

    @Command(command="router {name}")
    public RouterView getRouter(String name) {
        return new RouterView();
    }
}
