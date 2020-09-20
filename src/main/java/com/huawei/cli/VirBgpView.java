package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.BgpPeerGroupImpl;
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

    /*
        L3VPN通过import/export RT控制VRF之间是否互通，其作用和传统BGP Peer是一样的。
        前面已经用import/export RT实现了点到点BGP Peer，但是，VRF数量很多时需要执行
        大量BGP Peer命令，非常麻烦。传统BGP提出了路由反射器等多种方案，解决BGP Peer
        数量过多的问题。根据import/export RT技术的特点，抽象出BGP Peer Group模型，
        体现为两条配置命令，peer group {name} hub 和 peer group {name} spoke，将
        虚拟路由器以hub或spoke角色加入BGP Peer Group。
        为每个BGP Peer Group分配两个RT值，分别称为hubRT和spokeRT；VRF以hub角色加入
        Group时，export hubRT，import hubRT & spokeRT；VRF以spoke角色加入Group时，
        export SpokeRT，import HubRT。
     */
    @Command(command="peer group {name} hub")
    public void joinPeerGroupAsHub(String name) {
        vrf.addConfig(CLI.BGP);
        vrf.addConfig(CLI.__, CLI.PEER, CLI.GROUP, name, CLI.HUB);

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        BgpPeerGroupImpl group = VpnRes.getOrCreatePeerGroup(pr.getAsNumber(), name);
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getHubRT(), CLI.EXPORT);
        pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getHubRT(), CLI.IMPORT);
        pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getSpokeRT(), CLI.IMPORT);
    }

    @Command(command="peer group {name} spoke")
    public void joinPeerGroupAsSpoke(String name) {
        vrf.addConfig(CLI.BGP);
        vrf.addConfig(CLI.__, CLI.PEER, CLI.GROUP, name, CLI.SPOKE);

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        BgpPeerGroupImpl group = VpnRes.getOrCreatePeerGroup(pr.getAsNumber(), name);
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getSpokeRT(), CLI.EXPORT);
        pr.addConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getHubRT(), CLI.IMPORT);
    }
}
