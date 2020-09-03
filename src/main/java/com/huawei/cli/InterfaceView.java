package com.huawei.cli;

import wjl.cli.Command;
import wjl.cli.CommandView;

public class InterfaceView implements CommandView {
    @Override
    public String getPrompt() {
        return "interface";
    }

    @Command(command="ip address {ip}")
    public void setIpAddress(String ip) {

    }
}
