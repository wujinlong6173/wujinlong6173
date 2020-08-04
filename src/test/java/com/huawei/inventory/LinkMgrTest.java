package com.huawei.inventory;

import static org.junit.Assert.*;

import org.junit.Test;

public class LinkMgrTest {
    @Test
    public void getPortOfLink() {
        assertEquals("GE2/0", LinkMgr.getPortOfLink("link2", "AG02"));
        assertEquals("GE2/1", LinkMgr.getPortOfLink("link2", "AG03"));
        assertNull(LinkMgr.getPortOfLink("link2",  "AG04"));
        assertNull(LinkMgr.getPortOfLink("link99",  "AG04"));
    }
    
    @Test
    public void findLinkBetweenDevice() {
        assertEquals("link2", LinkMgr.findLinkBetweenDevice("AG02", "AG03"));
        assertEquals("link3", LinkMgr.findLinkBetweenDevice("AG04", "AG03"));
        assertNull(LinkMgr.findLinkBetweenDevice("AG04", "AG04"));
    }
    
    @Test
    public void allocVlanId() {
        assertEquals(1, LinkMgr.allocVlanId("link3"));
        assertEquals(2, LinkMgr.allocVlanId("link3"));
        assertEquals(3, LinkMgr.allocVlanId("link3"));
        assertEquals(1, LinkMgr.allocVlanId("link4"));
        assertEquals(-1, LinkMgr.allocVlanId("link99"));
    }
   
}
