package com.huawei.physical;

import org.junit.Test;
import wjl.provider.ProviderException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RefPortProviderTest extends PhyTestContainer {
    RefPortProvider rpp = new RefPortProvider("test", "RP");

    @Test
    public void createPort() throws ProviderException {
        PhyRouter pr = new PhyRouter();
        pr.setName("NE91");
        deviceMgr.addRouter(pr);

        rpp.setContainer(container);
        Map<String,Object> inputs = new HashMap<>();
        inputs.put("refPort", "GE1");
        rpp.create("NE91", "P2", inputs);
    }

    @Test
    public void createPort_NoRouter() {
        rpp.setContainer(container);
        try {
            Map<String, Object> inputs = new HashMap<>();
            inputs.put("refPort", "GE1");
            rpp.create("NE99", "P2", inputs);
            fail("should throw exception");
        } catch (ProviderException err) {
            assertTrue(err.getMessage().contains("not exist"));
        }
    }

    @Test
    public void createPort_config() {
        PhyRouter pr = new PhyRouter();
        pr.setName("NE93");
        deviceMgr.addRouter(pr);

        PhyLink lk = new PhyLink();
        lk.setId("link-1");
        lk.setSrcDevice("NE92");
        lk.setSrcPort("GE1");
        lk.setDstDevice("NE93");
        lk.setDstPort("GE2");
        linkMgr.addLink(lk);

        rpp.setContainer(container);
        try {
            Map<String,Object> inputs = new HashMap<>();
            inputs.put("refPort", "GE2");
            rpp.create("NE93", "P2", inputs);
            fail("should throw exception");
        } catch (ProviderException err) {
            assertTrue(err.getMessage().contains("is occupied."));
        }
    }
}