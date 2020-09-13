package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfMgr;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 配置虚拟路由器
 */
public class VirRouterView implements CommandView {
    // 虚拟路由器的内部标识
    private final String id;

    // 虚拟路由器的名称
    private final String name;

    @Override
    public String getPrompt() {
        return name;
    }

    VirRouterView(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Command(command="interface")
    public Object listInterfaces() {
        Vrf vrf = VrfMgr.getVrf(id);
        if (vrf == null) {
            return "Error : virtual router is deleted.";
        }

        List<String> ret = new ArrayList<>(vrf.getBindInterfaces().keySet());
        Collections.sort(ret);
        return ret;
    }

    @Command(command="interface {name}")
    public Object cfgInterface(String portName) {
        Vrf vrf = VrfMgr.getVrf(id);
        if (vrf == null) {
            return "Error : virtual router is deleted.";
        }

        Interface inf = vrf.getBindInterfaces().get(portName);
        if (inf == null) {
            return "Error : please create interface before config.";
        }

        return new VirInterfaceView(inf.getId(), portName);
    }

    @Command(command="ip static-route {dst} out {port} next-hop {nextIp}")
    public String addStaticRoute(String dst, String port, String nextIp) {
        Vrf vrf = VrfMgr.getVrf(id);
        if (vrf == null) {
            return "Error : virtual router is deleted.";
        }

        Interface inf = vrf.getBindInterfaces().get(port);
        if (inf == null) {
            return String.format(Locale.ENGLISH, "Error : port %s does not exist.", port);
        }

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.IP, CLI.STATIC_ROUTE, CLI.VPN_INSTANCE, vrf.getName(), dst,
                CLI.OUT, inf.getInterfaceName(), CLI.NEXT_HOP, nextIp);
        return null;
    }
}
