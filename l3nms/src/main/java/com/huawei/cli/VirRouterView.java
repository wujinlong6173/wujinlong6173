package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.common.Interface;
import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import com.huawei.vrf.Vrf;
import wjl.cli.Command;
import wjl.cli.CommandView;
import wjl.cli.ConfigHolder;
import wjl.docker.AbstractMember;

import java.util.*;

/**
 * 配置虚拟路由器
 */
public class VirRouterView extends AbstractMember implements CommandView {
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

        VirInterfaceView view = new VirInterfaceView(inf, portName);
        view.setContainer(this);
        return view;
    }

    @Command(command="ip static-route {dst} out {port} next-hop {nextIp}")
    public String addStaticRoute(String dst, String port, String nextIp) {
        Interface inf = vrf.getBindInterfaces().get(port);
        if (inf == null) {
            return String.format(Locale.ENGLISH, "Error : port %s does not exist.", port);
        }

        vrf.addCommand(CLI.IP, CLI.STATIC_ROUTE, dst, CLI.OUT, port, CLI.NEXT_HOP, nextIp);

        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(vrf.getHost());
        pr.addCommand(CLI.IP, CLI.STATIC_ROUTE, CLI.VPN_INSTANCE, vrf.getName(), dst,
                CLI.OUT, inf.getInterfaceName(), CLI.NEXT_HOP, nextIp);
        return null;
    }

    @Command(command="bgp")
    public Object cfgBgp() {
        vrf.addHolder(CLI.BGP);

        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(vrf.getHost());
        ConfigHolder bgp = pr.addHolder(CLI.BGP, pr.getAsNumber());
        bgp.addCommand(CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        VirBgpView view = new VirBgpView(vrf);
        view.setContainer(this);
        return view;
    }
}
