package com.huawei.cli;

import wjl.cli.Command;

/**
 * 管理物理网络
 */
public class AdminView {
    @Command(command="routers")
    public void listRouters() {

    }

    @Command(command="router")
    public RouterView getRouter(String name) {
        return new RouterView();
    }
}
