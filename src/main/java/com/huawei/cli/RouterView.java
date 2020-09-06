package com.huawei.cli;

import com.huawei.inventory.PhyRouter;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.List;

public class RouterView implements CommandView {
    private PhyRouter phyRouter;

    public RouterView(PhyRouter phyRouter) {
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

    @Command(command="interface")
    public List<String> listInterfaces() {
        return null;
    }

    @Command(command="ospf")
    public OspfView makeOspf() {
        return new OspfView();
    }

    @Command(command="bgp")
    public BgpView makeBgp() {
        return new BgpView();
    }
}
