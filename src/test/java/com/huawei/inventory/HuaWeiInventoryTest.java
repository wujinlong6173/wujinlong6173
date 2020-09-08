package com.huawei.inventory;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class HuaWeiInventoryTest {
    @Test
    public void testLoadFromFile() {
        HuaWeiInventory.loadFromFile("/huawei/inventory.yaml");
        assertTrue(PhyRouterMgr.isDeviceExist("AG01"));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice("AG02", "AG03"));

        Set<String> expDevices = new HashSet<>();
        expDevices.add("AG01");
        expDevices.add("AG02");
        expDevices.add("AG03");
        expDevices.add("AG04");
        expDevices.add("AG05");
        assertEquals(expDevices, PhyRouterMgr.getDevices());

        PhyRouterMgr.clear();
        PhyLinkMgr.clear();
    }
}
