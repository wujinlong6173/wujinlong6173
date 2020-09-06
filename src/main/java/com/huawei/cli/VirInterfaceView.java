package com.huawei.cli;

import wjl.cli.Command;
import wjl.cli.CommandView;

/**
 * 配置虚拟路由器的接口
 */
public class VirInterfaceView implements CommandView {
    // 接口的内部标识
    private String id;

    // 接口在虚拟路由器中的名称
    private String name;

    VirInterfaceView(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getPrompt() {
        return name;
    }

    @Command(command="ip address {ip}")
    public void setIpAddress(String ip) {

    }
}
