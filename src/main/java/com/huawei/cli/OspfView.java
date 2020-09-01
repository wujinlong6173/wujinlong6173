package com.huawei.cli;

import wjl.cli.Command;

public class OspfView {
    @Command(command="area")
    public OspfAreaView makeArea(Integer id) {
        return new OspfAreaView();
    }
}
