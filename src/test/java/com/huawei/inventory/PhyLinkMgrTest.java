package com.huawei.inventory;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhyLinkMgrTest {
    @Test
    public void testFindLink() {
        PhyLink k1 = new PhyLink();
        k1.setId("link-mgr-1");
        k1.setSrcDevice("AG1");
        k1.setDstDevice("AG2");
        k1.setSrcPort("GE1");
        k1.setDstPort("GE2");
        PhyLinkMgr.addLink(k1);

        PhyLink k2 = new PhyLink();
        k2.setId("link-mgr-2");
        k2.setSrcDevice("AG1");
        k2.setDstDevice("AG3");
        k2.setSrcPort("GE1");
        k2.setDstPort("GE3");
        PhyLinkMgr.addLink(k2);

        PhyLink k3 = new PhyLink();
        k3.setId("link-mgr-3");
        k3.setSrcDevice("AG3");
        k3.setDstDevice("AG2");
        k3.setSrcPort("GE3");
        k3.setDstPort("GE2");
        PhyLinkMgr.addLink(k3);

        assertSame(k1, PhyLinkMgr.findLinkBetweenDevice("AG1", "AG2"));
        assertSame(k1, PhyLinkMgr.findLinkBetweenDevice("AG2", "AG1"));
        assertSame(k3, PhyLinkMgr.findLinkBetweenDevice("AG3", "AG2"));
        assertNull(PhyLinkMgr.findLinkBetweenDevice("AG1", "AG9"));
    }
}