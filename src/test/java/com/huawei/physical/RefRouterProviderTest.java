package com.huawei.physical;

import org.junit.Test;
import wjl.provider.ProviderException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RefRouterProviderTest extends PhyTestContainer {
    RefRouterProvider rrp = new RefRouterProvider("test", "RR");

    @Test
    public void refRouter() throws ProviderException {
        PhyRouter r1 = new PhyRouter();
        r1.setName("CX600-1");
        deviceMgr.addRouter(r1);
        rrp.setContainer(container);

        Map<String, Object> inputs = new HashMap<>();
        inputs.put("refRouterName", "CX600-1");
        assertEquals("CX600-1", rrp.create("no-use", inputs));
    }

    @Test
    public void refRouter_NotExist() {
        rrp.setContainer(container);

        Map<String, Object> inputs = new HashMap<>();
        inputs.put("refRouterName", "CX600-9");
        try {
            rrp.create("no-use", inputs);
            fail("should throw exception");
        } catch (ProviderException err) {
            assertTrue(err.getMessage().contains("CX600-9"));
        }
    }
}