package com.huawei.vrf;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.huawei.VirTestContainer;
import org.junit.Test;

import wjl.provider.ProviderException;

public class VrfDeviceProviderTest extends VirTestContainer {
    @Test
    public void createVrf() throws ProviderException {
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "VrfTest1");
        routerProvider.create(UUID.randomUUID().toString(), inputs);
        inputs.put("name", "VrfTest2");
        routerProvider.create(UUID.randomUUID().toString(), inputs);

        inputs = new HashMap<>();
        inputs.put("host", "VrfTest1");
        inputs.put("name", "4g.vpn");
        String id = vrfProvider.create("---", inputs);
        assertEquals("VrfTest1", vrfMgr.getHostOfVrf(id));

        inputs.put("host", "VrfTest2");
        inputs.put("name", "4g.vpn");
        id = vrfProvider.create("---", inputs);
        assertEquals("VrfTest2", vrfMgr.getHostOfVrf(id));
    }
}
