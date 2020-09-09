package com.huawei.inventory;

import com.huawei.common.CLI;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PhyRouterTest {
    @Test
    public void cfgOneLevel() {
        PhyRouter pr = new PhyRouter();
        pr.addConfig("mpls");
        pr.addConfig("mpls", "ldp");
        pr.addConfig("interface", "GE1/0.100");
        pr.addConfig("interface", "GE1/0.200");
        pr.addConfig("ip", "vpn-instance", "test");

        // 重复执行一遍
        pr.addConfig("mpls");
        pr.addConfig("mpls", "ldp");
        pr.addConfig("interface", "GE1/0.100");
        pr.addConfig("interface", "GE1/0.200");
        pr.addConfig("ip", "vpn-instance", "test");

        List<String> exp = new ArrayList<>();
        exp.add("mpls");
        exp.add("mpls ldp");
        exp.add("interface GE1/0.100");
        exp.add("interface GE1/0.200");
        exp.add("ip vpn-instance test");

        assertEquals(exp, pr.getConfigs());
    }

    @Test
    public void cfgTwoLevel() {
        PhyRouter pr = new PhyRouter();
        pr.addConfig("interface", "GE1/0.100");
        pr.addConfig(CLI.__, "encapsulation", "dot1q", "100");
        pr.addConfig("interface", "GE1/0.200");
        pr.addConfig(CLI.__, "encapsulation", "dot1q", "200");

        pr.addConfig("interface", "GE1/0.100");
        pr.addConfig(CLI.__, "ip", "binding", "vpn-instance", "a");
        pr.addConfig(CLI.__, "ip", "address", "192.168.0.100");
        pr.addConfig("interface", "GE1/0.200");
        pr.addConfig(CLI.__, "ip", "binding", "vpn-instance", "b");
        pr.addConfig(CLI.__, "ip", "address", "192.168.0.200");

        List<String> exp = new ArrayList<>();
        exp.add("interface GE1/0.100");
        exp.add("   encapsulation dot1q 100");
        exp.add("   ip binding vpn-instance a");
        exp.add("   ip address 192.168.0.100");
        exp.add("interface GE1/0.200");
        exp.add("   encapsulation dot1q 200");
        exp.add("   ip binding vpn-instance b");
        exp.add("   ip address 192.168.0.200");
        assertEquals(exp, pr.getConfigs());
    }
}