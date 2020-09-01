package com.huawei.cli;

import wjl.cli.Command;

public class RouterView {
    @Command(command="interface")
    public InterfaceView makeInterface(String name) {
        return new InterfaceView();
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
