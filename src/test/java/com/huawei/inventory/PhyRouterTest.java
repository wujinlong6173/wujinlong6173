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

        // 下面撤销配置

        pr.undoConfig("interface", "GE1/0.100");
        pr.undoConfig("mpls", "ldp");
        exp.remove(2);
        exp.remove(1);
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

        // 下面撤销配置

        pr.undoConfig("interface", "GE1/0.100");
        exp.remove(3);
        exp.remove(2);
        exp.remove(1);
        exp.remove(0);
        assertEquals(exp, pr.getConfigs());
    }

    @Test
    public void cfgThreeLevel() {
        PhyRouter pr = new PhyRouter();
        pr.addConfig("ospf", "100");
        pr.addConfig(CLI.__, "area", "0.0.1.0");
        pr.addConfig(CLI.__, CLI.__, "network", "192.168.1.0");
        pr.addConfig(CLI.__, CLI.__, "network", "192.168.2.0");
        pr.addConfig(CLI.__, "area", "0.0.1.1");
        pr.addConfig(CLI.__, CLI.__, "network", "192.168.3.0");
        pr.addConfig(CLI.__, CLI.__, "network", "192.168.4.0");
        pr.addConfig("ospf", "200");
        pr.addConfig(CLI.__, "area", "0.0.2.0");
        pr.addConfig(CLI.__, "area", "0.0.2.1");

        List<String> exp = new ArrayList<>();
        exp.add("ospf 100");
        exp.add("   area 0.0.1.0");
        exp.add("      network 192.168.1.0");
        exp.add("      network 192.168.2.0");
        exp.add("   area 0.0.1.1");
        exp.add("      network 192.168.3.0");
        exp.add("      network 192.168.4.0");
        exp.add("ospf 200");
        exp.add("   area 0.0.2.0");
        exp.add("   area 0.0.2.1");
        assertEquals(exp, pr.getConfigs());

        pr.addConfig("ospf", "100");
        pr.addConfig(CLI.__, "area", "0.0.1.0");
        pr.addConfig(CLI.__, CLI.__, "network", "192.168.5.0");
        exp.add(4, "      network 192.168.5.0");
        assertEquals(exp, pr.getConfigs());

        // 下面撤销配置

        pr.undoConfig(CLI.__, "area", "0.0.1.0");
        exp.clear();
        exp.add("ospf 100");
        exp.add("   area 0.0.1.1");
        exp.add("      network 192.168.3.0");
        exp.add("      network 192.168.4.0");
        exp.add("ospf 200");
        exp.add("   area 0.0.2.0");
        exp.add("   area 0.0.2.1");
        assertEquals(exp, pr.getConfigs());

        pr.addConfig(CLI.__, "area", "0.0.1.1");
        pr.undoConfig(CLI.__, CLI.__, "network", "192.168.3.0");
        exp.clear();
        exp.add("ospf 100");
        exp.add("   area 0.0.1.1");
        exp.add("      network 192.168.4.0");
        exp.add("ospf 200");
        exp.add("   area 0.0.2.0");
        exp.add("   area 0.0.2.1");
        assertEquals(exp, pr.getConfigs());
    }
}