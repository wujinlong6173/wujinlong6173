package com.huawei.inventory;

import static org.junit.Assert.*;

import org.junit.Test;

public class HuaWeiInventoryTest {
    @Test
    public void testLoadFromFile() {
        HuaWeiInventory.loadFromFile("/huawei/inventory.yaml");
        assertTrue(PhyRouterMgr.isDeviceExist("AG01"));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice("AG02", "AG03"));
    }
}
