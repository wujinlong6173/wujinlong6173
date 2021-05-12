package com.huawei.physical;

import com.huawei.common.CLI;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.List;

public class PhyRouterView implements CommandView {
    private PhyRouter phyRouter;

    public PhyRouterView(PhyRouter phyRouter) {
        this.phyRouter = phyRouter;
    }

    @Override
    public String getPrompt() {
        return phyRouter.getName();
    }

    @Command(command="display")
    public List<String> displayConfigs() {
        return phyRouter.getConfigs();
    }

    @Command(command="bgp {asNumber}")
    public void makeBgp(String asNumber) {
        phyRouter.setAsNumber(asNumber);
        phyRouter.addHolder(CLI.BGP, asNumber);
    }
}
