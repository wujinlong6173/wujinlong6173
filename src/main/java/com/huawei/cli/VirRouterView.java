package com.huawei.cli;

import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfMgr;
import wjl.cli.Command;
import wjl.cli.CommandView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VirRouterView implements CommandView {
    private final String id;
    private final String name;

    @Override
    public String getPrompt() {
        return name;
    }

    VirRouterView(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Command(command="interface")
    public Object listInterfaces() {
        Vrf vrf = VrfMgr.getVrf(id);
        if (vrf == null) {
            return "virtual router is deleted.";
        }

        List<String> ret = new ArrayList<>(vrf.getBindInterfaces().keySet());
        Collections.sort(ret);
        return ret;
    }
}
