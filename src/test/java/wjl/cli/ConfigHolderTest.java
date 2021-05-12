package wjl.cli;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConfigHolderTest {
    @Test
    public void cfgOneLevel() {
        ConfigHolder pr = new ConfigHolder();
        pr.addCommand("mpls");
        pr.addCommand("mpls", "ldp");
        pr.addHolder("interface", "GE1/0.100");
        pr.addHolder("interface", "GE1/0.200");
        pr.addHolder("ip", "vpn-instance", "test");

        // 重复执行一遍
        pr.addCommand("mpls");
        pr.addCommand("mpls", "ldp");
        pr.addHolder("interface", "GE1/0.100");
        pr.addHolder("interface", "GE1/0.200");
        pr.addHolder("ip", "vpn-instance", "test");

        List<String> exp = new ArrayList<>();
        exp.add("mpls");
        exp.add("mpls ldp");
        exp.add("interface GE1/0.100");
        exp.add("interface GE1/0.200");
        exp.add("ip vpn-instance test");
        assertEquals(exp, pr.getConfigs());

        // 下面撤销配置

        pr.undo("interface", "GE1/0.100");
        pr.undo("mpls", "ldp");
        exp.remove(2);
        exp.remove(1);
        assertEquals(exp, pr.getConfigs());
    }

    @Test
    public void cfgTwoLevel() {
        ConfigHolder pr = new ConfigHolder();
        ConfigHolder inf = pr.addHolder("interface", "GE1/0.100");
        inf.addCommand("encapsulation", "dot1q", "100");
        inf = pr.addHolder("interface", "GE1/0.200");
        inf.addCommand("encapsulation", "dot1q", "200");

        inf = pr.addHolder("interface", "GE1/0.100");
        inf.addCommand("ip", "binding", "vpn-instance", "a");
        inf.addCommand("ip", "address", "192.168.0.100");
        inf = pr.addHolder("interface", "GE1/0.200");
        inf.addCommand("ip", "binding", "vpn-instance", "b");
        inf.addCommand("ip", "address", "192.168.0.200");

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

        pr.undo("interface", "GE1/0.100");
        exp.remove(3);
        exp.remove(2);
        exp.remove(1);
        exp.remove(0);
        assertEquals(exp, pr.getConfigs());
    }

    @Test
    public void cfgThreeLevel() {
        ConfigHolder pr = new ConfigHolder();
        ConfigHolder ospf = pr.addHolder("ospf", "100");
        ConfigHolder area = ospf.addHolder("area", "0.0.1.0");
        area.addCommand("network", "192.168.1.0");
        area.addCommand("network", "192.168.2.0");
        area = ospf.addHolder("area", "0.0.1.1");
        area.addCommand("network", "192.168.3.0");
        area.addCommand("network", "192.168.4.0");
        ospf = pr.addHolder("ospf", "200");
        ospf.addHolder("area", "0.0.2.0");
        ospf.addHolder("area", "0.0.2.1");

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

        ospf = pr.addHolder("ospf", "100");
        area = ospf.addHolder("area", "0.0.1.0");
        area.addCommand("network", "192.168.5.0");
        exp.add(4, "      network 192.168.5.0");
        assertEquals(exp, pr.getConfigs());

        // 下面撤销配置
        ospf.undo("area", "0.0.1.0");
        exp.clear();
        exp.add("ospf 100");
        exp.add("   area 0.0.1.1");
        exp.add("      network 192.168.3.0");
        exp.add("      network 192.168.4.0");
        exp.add("ospf 200");
        exp.add("   area 0.0.2.0");
        exp.add("   area 0.0.2.1");
        assertEquals(exp, pr.getConfigs());

        area = ospf.addHolder("area", "0.0.1.1");
        area.undo("network", "192.168.3.0");
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