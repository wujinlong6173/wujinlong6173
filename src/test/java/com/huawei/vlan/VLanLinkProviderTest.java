package com.huawei.vlan;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.huawei.physical.PhyLinkMgr;
import com.huawei.physical.PhyLinkProvider;
import com.huawei.physical.PhyDeviceMgr;
import com.huawei.physical.PhyRouterProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfDeviceProvider;
import com.huawei.vrf.VrfMgr;

import wjl.provider.ProviderException;

import static org.junit.Assert.*;

public class VLanLinkProviderTest {
    @Before
    public void init() throws ProviderException {
        PhyRouterProvider routerProvider = new PhyRouterProvider();
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "VLanTest1");
        String r1 = routerProvider.create(UUID.randomUUID().toString(), inputs);
        inputs.put("name", "VLanTest2");
        String r2 = routerProvider.create(UUID.randomUUID().toString(), inputs);

        PhyLinkProvider linkProvider = new PhyLinkProvider();
        linkProvider.create(UUID.randomUUID().toString(), r1, "GE0", null, r2, "GE1", null, inputs);
    }

    @After
    public void fini() {
        PhyDeviceMgr.clear();
        PhyLinkMgr.clear();
    }

    @Test
    public void createVLanLink() throws ProviderException {
        Map<String,Object> inputs = new HashMap<>();
        
        // 准备数据
        VrfDeviceProvider vrfProvider = new VrfDeviceProvider();
        inputs.put("host", "VLanTest1");
        inputs.put("name", "4g.vpn");
        String r1 = vrfProvider.create("---", inputs);
        inputs.put("host", "VLanTest2");
        inputs.put("name", "4g.vpn");
        String r2 = vrfProvider.create("---", inputs);
        
        VLanLinkProvider provider = new VLanLinkProvider();

        // 创建链路
        inputs.clear();
        String lk = provider.create("---", 
                r1, "Eth1", "VRF",
                r2, "Eth2", "VRF",
                inputs);
        
        Vrf vrf1 = VrfMgr.getVrf(r1);
        assertTrue(vrf1.getBindInterfaces().containsKey("Eth1"));
        Vrf vrf2 = VrfMgr.getVrf(r2);
        assertTrue(vrf2.getBindInterfaces().containsKey("Eth2"));

        // 删除链路
        provider.delete(lk, inputs);
        assertNull(VLanDao.getVLanLink(lk));
        assertTrue(vrf1.getBindInterfaces().isEmpty());
        assertTrue(vrf2.getBindInterfaces().isEmpty());
    }
}

