package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.common.InterfaceMgr;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
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
    public String setIpAddress(String ip) {
        Interface inf = InterfaceMgr.getInterface(id);
        if (inf == null) {
            return "Error : virtual interface is deleted.";
        }

        PhyRouter pr = PhyRouterMgr.getRouter(inf.getHost());
        pr.addConfig(CLI.INTERFACE, inf.getInterfaceName());
        pr.addConfig(CLI.__, CLI.IP, CLI.ADDRESS, ip);
        return null;
    }
}
