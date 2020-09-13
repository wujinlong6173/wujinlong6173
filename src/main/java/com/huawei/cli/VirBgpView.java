package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.Vrf;
import wjl.cli.Command;
import wjl.cli.CommandView;

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

    @Command(command="peer {peer} as-number {as}")
    public void addPeer(String peer, String as) {
        vrf.addConfig(CLI.BGP);
        vrf.addConfig(CLI.__, CLI.PEER, peer, CLI.AS_NUMBER, as);

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.addConfig(CLI.BGP, pr.getAsNumber());
        pr.addConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, vrf.getName());
        pr.addConfig(CLI.__, CLI.__, CLI.PEER, peer, CLI.AS_NUMBER, as);
    }
}
