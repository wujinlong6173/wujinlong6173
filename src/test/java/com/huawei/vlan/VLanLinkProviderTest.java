package com.huawei.vlan;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.huawei.vrf.VrfDeviceProvider;

import wjl.net.provider.ProviderException;

public class VLanLinkProviderTest {
    @Test
    public void createVlanLink() throws ProviderException {
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
        
        inputs.clear();
        String lk = provider.create("---", 
                r1, "Eth1", vrfProvider.getName(),
                r2, "Eth2", vrfProvider.getName(),
                inputs);
    }
}
