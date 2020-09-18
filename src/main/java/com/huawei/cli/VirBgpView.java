package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.VpnRes;
import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfMgr;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.Locale;

public class VirBgpView implements CommandView {
    private final Vrf vrf;

    VirBgpView(Vrf vrf) {
        this.vrf = vrf;
    }

    @Override
    public String getPrompt() {
        return "bgp";
    }

    @Command(command="import-route {witch}")
    public void importRoute(String witch) {
        vrf.addConfig(CLI.BGP);
        vrf.addConfig(CLI.__, CLI.IMPORT_ROUTE, witch);

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        pr.addConfig(CLI.__, CLI.__, CLI.IMPORT_ROUTE, witch);
    }

    /*
        设置BGP Router ID的目的，是为了在两个VRF之间配置BGP Peer。
        */
    @Command(command="router-id {ip}")
    public String setRouterId(String ip) {
        Vrf conflict = VrfMgr.getVrfByBgpRouterId(ip);
        if (conflict != null) {
            return ip + " is used by other router.";
        }

        vrf.setBgpRouterId(ip);
        vrf.addConfig(CLI.BGP);
        vrf.addConfig(CLI.__, CLI.ROUTER_ID, ip);

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        pr.addConfig(CLI.__, CLI.__, CLI.ROUTER_ID, ip);
        return null;
    }

    /*
        如果AS号相同，两端肯定属于同一个运营商，而且是单域L3VPN内部互通。
        如果AS号不同，可能是L3VPN Optional C跨域互通，系统应该管理一个AS列表，列表中的AS支持跨域互通。
        其它情况，肯定是普通的BPG Peer，简单配置即可。
        */
    @Command(command="peer {peer} as-number {as}")
    public String addPeer(String peer, String as) {
        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        if (pr.getAsNumber().equals(as)) {
            Vrf other = VrfMgr.getVrfByBgpRouterId(peer);
            if (other == null) {
                return String.format(Locale.ENGLISH, "peer %s does not exist.", peer);
            }

            if (other == vrf) {
                return "can not add peer to the route itself.";
            }

            String otherRt = cfgRtForBgpPeer(other);
            // 本端VRF导入对端VRF的RT
            pr.addConfig(CLI.BGP, pr.getAsNumber());
            pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
            pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, otherRt, CLI.IMPORT);
        } else {
            pr.addConfig(CLI.BGP, pr.getAsNumber());
            pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
            pr.addConfig(CLI.__, CLI.__, CLI.PEER, peer, CLI.AS_NUMBER, as);
        }

        vrf.addConfig(CLI.BGP);
        vrf.addConfig(CLI.__, CLI.PEER, peer, CLI.AS_NUMBER, as);
        return null;
    }

    private String cfgRtForBgpPeer(Vrf other) {
        PhyRouter pr = PhyRouterMgr.getRouter(other.getHost());

        // 给对端VRF分配并导出RT
        if (other.getRtForBgpPeer() == null) {
            int newRt = VpnRes.allocRT();
            String fullRt = String.format("%s:%d", pr.getAsNumber(), newRt);
            other.setRtForBgpPeer(fullRt);

            pr.addConfig(CLI.BGP, pr.getAsNumber());
            pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, other.getName());
            pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, fullRt, CLI.EXPORT);
        }

        return other.getRtForBgpPeer();
    }
}