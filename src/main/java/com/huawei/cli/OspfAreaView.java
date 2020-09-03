package com.huawei.cli;

import wjl.cli.Command;
import wjl.cli.CommandView;

public class OspfAreaView implements CommandView {
    @Override
    public String getPrompt() {
        return "area";
    }

    @Command(command="area {ip} {mask}")
    public void makeNetwork(String ip, String mask) {
    }
}
