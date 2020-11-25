package com.huawei.vrf;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.huawei.physical.PhyLinkMgr;
import com.huawei.physical.PhyDeviceMgr;
import com.huawei.physical.PhyRouterProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import wjl.provider.ProviderException;

public class VrfDeviceProviderTest {
    @Before
    public void init() throws ProviderException {
        PhyRouterProvider routerProvider = new PhyRouterProvider();
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "VrfTest1");
        routerProvider.create(UUID.randomUUID().toString(), inputs);
        inputs.put("name", "VrfTest2");
        routerProvider.create(UUID.randomUUID().toString(), inputs);
    }

    @After
    public void fini() {
        PhyDeviceMgr.clear();
        PhyLinkMgr.clear();
    }

    @Test
    public void createVrf() throws ProviderException {
        VrfDeviceProvider provider = new VrfDeviceProvider();
        Map<String,Object> inputs = new HashMap<>();
        inputs.put("host", "VrfTest1");
        inputs.put("name", "4g.vpn");
        String id = provider.create("---", inputs);
        assertEquals("VrfTest1", VrfMgr.getHostOfVrf(id));

        inputs.put("host", "VrfTest2");
        inputs.put("name", "4g.vpn");
        id = provider.create("---", inputs);
        assertEquals("VrfTest2", VrfMgr.getHostOfVrf(id));
    }
}
