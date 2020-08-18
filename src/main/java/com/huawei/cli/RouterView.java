package com.huawei.cli;

import wjl.cli.CommandView;

public class RouterView {
    @CommandView(command="interface")
    public InterfaceView makeInterface(String name) {
        return new InterfaceView();
    }

    @CommandView(command="ospf")
    public OspfView makeOspf() {
        return new OspfView();
    }

    @CommandView(command="bgp")
    public BgpView makeBgp() {
        return new BgpView();
    }
}
