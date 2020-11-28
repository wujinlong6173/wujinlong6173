package com.huawei.vlan;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.huawei.VirTestContainer;
import org.junit.Test;

import com.huawei.vrf.Vrf;

import wjl.provider.ProviderException;

import static org.junit.Assert.*;

public class VLanLinkProviderTest extends VirTestContainer {
    @Test
    public void createVLanLink() throws ProviderException {
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "VLanTest1");
        String pr1 = routerProvider.create(UUID.randomUUID().toString(), inputs);
        inputs.put("name", "VLanTest2");
        String pr2 = routerProvider.create(UUID.randomUUID().toString(), inputs);

        linkProvider.create(UUID.randomUUID().toString(), pr1, "GE0", null, pr2, "GE1", null, inputs);

        inputs = new HashMap<>();
        
        // 准备数据

        inputs.put("host", "VLanTest1");
        inputs.put("name", "4g.vpn");
        String r1 = vrfProvider.create("---", inputs);
        inputs.put("host", "VLanTest2");
        inputs.put("name", "4g.vpn");
        String r2 = vrfProvider.create("---", inputs);
        
        VLanLinkProvider provider = new VLanLinkProvider(null, null);
        provider.setContainer(container);

        // 创建链路
        inputs.clear();
        String lk = provider.create("---", 
                r1, "Eth1", "VRF",
                r2, "Eth2", "VRF",
                inputs);
        
        Vrf vrf1 = vrfMgr.getVrf(r1);
        assertTrue(vrf1.getBindInterfaces().containsKey("Eth1"));
        Vrf vrf2 = vrfMgr.getVrf(r2);
        assertTrue(vrf2.getBindInterfaces().containsKey("Eth2"));

        // 删除链路
        provider.delete(lk, inputs);
        assertNull(VLanDao.getVLanLink(lk));
        assertTrue(vrf1.getBindInterfaces().isEmpty());
        assertTrue(vrf2.getBindInterfaces().isEmpty());
    }
}

