package com.huawei.vrf;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.huawei.inventory.HuaWeiInventory;
import com.huawei.inventory.PhyLinkMgr;
import com.huawei.inventory.PhyRouterMgr;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wjl.provider.ProviderException;

public class VrfDeviceProviderTest {
    @Before
    public void init() {
        HuaWeiInventory.loadFromFile("/huawei/inventory.yaml");
    }

    @After
    public void fini() {
        PhyRouterMgr.clear();
        PhyLinkMgr.clear();
    }

    @Test
    public void createVrf() throws ProviderException {
        VrfDeviceProvider provider = new VrfDeviceProvider();
        Map<String,Object> inputs = new HashMap<>();
        inputs.put("host", "AG02");
        inputs.put("name", "4g.vpn");
        String id = provider.create("---", inputs);
        assertEquals("AG02", VrfMgr.getHostOfVrf(id));
        
        inputs.put("host", "AG03");
        inputs.put("name", "4g.vpn");
        id = provider.create("---", inputs);
        assertEquals("AG03", VrfMgr.getHostOfVrf(id));
    }
}
