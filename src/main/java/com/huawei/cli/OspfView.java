package com.huawei.cli;

import wjl.cli.CommandView;

public class OspfView {
    @CommandView(command="area")
    public OspfAreaView makeArea(Integer id) {
        return new OspfAreaView();
    }
}
