package com.huawei.inventory;

import static org.junit.Assert.*;

import org.junit.Test;

public class HuaWeiInventoryTest {
    @Test
    public void getPortOfLink() {
        assertEquals("GE2/0", HuaWeiInventory.getPortOfLink("link2", "AG02"));
        assertEquals("GE2/1", HuaWeiInventory.getPortOfLink("link2", "AG03"));
        assertNull(HuaWeiInventory.getPortOfLink("link2",  "AG04"));
        assertNull(HuaWeiInventory.getPortOfLink("link99",  "AG04"));
    }
    
    @Test
    public void findLinkBetweenDevice() {
        assertEquals("link2", HuaWeiInventory.findLinkBetweenDevice("AG02", "AG03"));
        assertEquals("link3", HuaWeiInventory.findLinkBetweenDevice("AG04", "AG03"));
        assertNull(HuaWeiInventory.findLinkBetweenDevice("AG04", "AG04"));
    }
    
    @Test
    public void allocVlanId() {
        assertEquals(1, HuaWeiInventory.allocVlanId("link3"));
        assertEquals(2, HuaWeiInventory.allocVlanId("link3"));
        assertEquals(3, HuaWeiInventory.allocVlanId("link3"));
        assertEquals(1, HuaWeiInventory.allocVlanId("link4"));
        assertEquals(-1, HuaWeiInventory.allocVlanId("link99"));
    }
   
}
