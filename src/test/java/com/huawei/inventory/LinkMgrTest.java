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
}
