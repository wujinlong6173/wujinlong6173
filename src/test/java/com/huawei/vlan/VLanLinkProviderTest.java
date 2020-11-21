package com.huawei.vlan;

import java.util.HashMap;
import java.util.Map;

import com.huawei.inventory.HuaWeiInventory;
import com.huawei.inventory.PhyLinkMgr;
import com.huawei.inventory.PhyRouterMgr;
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
    public void init() {
        HuaWeiInventory.loadFromFile("/huawei/inventory.yaml");
    }

    @After
    public void fini() {
        PhyRouterMgr.clear();
        PhyLinkMgr.clear();
    }

    @Test
    public void createVLanLink() throws ProviderException {
        Map<String,Object> inputs = new HashMap<>();
        
        // 准备数据
        VrfDeviceProvider vrfProvider = new VrfDeviceProvider();
        inputs.put("host", "AG01");
        inputs.put("name", "4g.vpn");
        String r1 = vrfProvider.create("---", inputs);
        inputs.put("host", "AG02");
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

