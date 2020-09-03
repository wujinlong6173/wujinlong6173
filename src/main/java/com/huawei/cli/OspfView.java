package com.huawei.cli;

import wjl.cli.Command;
import wjl.cli.CommandView;

public class OspfView implements CommandView {
    @Override
    public String getPrompt() {
        return "ospf";
    }

    @Command(command="area {id}")
    public OspfAreaView makeArea(Integer id) {
        return new OspfAreaView();
    }
}
