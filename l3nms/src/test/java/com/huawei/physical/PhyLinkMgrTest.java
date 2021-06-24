package com.huawei.physical;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhyLinkMgrTest {
    @Test
    public void findLink() {
        PhyLinkMgr linkMgr = new PhyLinkMgr();

        PhyLink k1 = new PhyLink();
        k1.setId("link-mgr-1");
        k1.setSrcDevice("AG1");
        k1.setDstDevice("AG2");
        k1.setSrcPort("GE1");
        k1.setDstPort("GE2");
        linkMgr.addLink(k1);

        PhyLink k2 = new PhyLink();
        k2.setId("link-mgr-2");
        k2.setSrcDevice("AG1");
        k2.setDstDevice("AG3");
        k2.setSrcPort("GE2");
        k2.setDstPort("GE1");
        linkMgr.addLink(k2);

        PhyLink k3 = new PhyLink();
        k3.setId("link-mgr-3");
        k3.setSrcDevice("AG3");
        k3.setDstDevice("AG2");
        k3.setSrcPort("GE2");
        k3.setDstPort("GE1");
        linkMgr.addLink(k3);

        assertSame(k1, linkMgr.findLinkBetweenDevice("AG1", "AG2"));
        assertSame(k1, linkMgr.findLinkBetweenDevice("AG2", "AG1"));
        assertSame(k3, linkMgr.findLinkBetweenDevice("AG3", "AG2"));
        assertNull(linkMgr.findLinkBetweenDevice("AG1", "AG9"));

        assertSame(k1, linkMgr.findLinkOnPort("AG1", "GE1"));
        assertSame(k2, linkMgr.findLinkOnPort("AG1", "GE2"));
        assertNull(linkMgr.findLinkOnPort("AG2", "GE3"));
    }
}