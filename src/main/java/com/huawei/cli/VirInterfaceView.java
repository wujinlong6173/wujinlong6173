package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import wjl.cli.Command;
import wjl.cli.CommandView;

/**
 * 配置虚拟路由器的接口
 */
public class VirInterfaceView implements CommandView {
    // 接口的内部标识
    private final Interface inf;

    // 接口在虚拟路由器中的名称
    private final String name;

    VirInterfaceView(Interface vrf, String name) {
        this.inf = vrf;
        this.name = name;
    }

    @Override
    public String getPrompt() {
        return name;
    }

    @Command(command="ip address {ip}")
    public String setIpAddress(String ip) {
        PhyRouter pr = PhyRouterMgr.getRouter(inf.getHost());
        pr.addConfig(CLI.INTERFACE, inf.getInterfaceName());
        pr.addConfig(CLI.__, CLI.IP, CLI.ADDRESS, ip);
        return null;
    }
}
