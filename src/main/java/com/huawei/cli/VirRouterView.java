package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import com.huawei.vrf.Vrf;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.*;

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

    @Command(command="display")
    public List<String> displayConfigs() {
        List<String> ret = vrf.getConfigs();
        for (Map.Entry<String,Interface> entry : vrf.getBindInterfaces().entrySet()) {
            ret.add("interface " + entry.getKey());
            for (String infCfg : entry.getValue().getConfigs()) {
                ret.add("   " + infCfg);
            }
        }
        return ret;
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

        vrf.addConfig(CLI.IP, CLI.STATIC_ROUTE, dst, CLI.OUT, port, CLI.NEXT_HOP, nextIp);

        PhyRouter pr = PhyDeviceMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.IP, CLI.STATIC_ROUTE, CLI.VPN_INSTANCE, vrf.getName(), dst,
                CLI.OUT, inf.getInterfaceName(), CLI.NEXT_HOP, nextIp);
        return null;
    }

    @Command(command="bgp")
    public Object cfgBgp() {
        vrf.addConfig(CLI.BGP);

        PhyRouter pr = PhyDeviceMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        return new VirBgpView(vrf);
    }
}
