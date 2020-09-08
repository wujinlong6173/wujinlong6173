package com.huawei.inventory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PhyRouterTest {
    @Test
    public void testAddConfig() {
        PhyRouter pr = new PhyRouter();
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
}