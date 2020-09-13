package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.Vrf;
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
    private final Vrf vrf;

    @Override
    public String getPrompt() {
        return vrf.getName();
    }

    VirRouterView(Vrf vrf) {
        this.vrf = vrf;
    }

    @Command(command="interface")
    public Object listInterfaces() {
        List<String> ret = new ArrayList<>(vrf.getBindInterfaces().keySet());
        Collections.sort(ret);
        return ret;
    }

    @Command(command="interface {name}")
    public Object cfgInterface(String portName) {
        Interface inf = vrf.getBindInterfaces().get(portName);
        if (inf == null) {
            return "Error : please create interface before config.";
        }

        return new VirInterfaceView(inf, portName);
    }

    @Command(command="ip static-route {dst} out {port} next-hop {nextIp}")
    public String addStaticRoute(String dst, String port, String nextIp) {
        Interface inf = vrf.getBindInterfaces().get(port);
        if (inf == null) {
            return String.format(Locale.ENGLISH, "Error : port %s does not exist.", port);
        }

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.IP, CLI.STATIC_ROUTE, CLI.VPN_INSTANCE, vrf.getName(), dst,
                CLI.OUT, inf.getInterfaceName(), CLI.NEXT_HOP, nextIp);
        return null;
    }

    @Command(command="bgp")
    public Object cfgBgp() {
        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        return new VirBgpView(vrf);
    }
}
