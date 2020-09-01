package com.huawei.cli;

import wjl.cli.Command;

public class OspfView {
    @Command(command="area {id}")
    public OspfAreaView makeArea(Integer id) {
        return new OspfAreaView();
    }
}
