package com.huawei.cli;

import wjl.cli.CommandView;

public class SystemView {
    @CommandView(command="router")
    public RouterView getRouter(String name) {
        return new RouterView();
    }
}
