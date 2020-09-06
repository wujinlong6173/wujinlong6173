package com.huawei.cli;

import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.List;

public class RouterView implements CommandView {
    @Override
    public String getPrompt() {
        return "router";
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
