package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.inventory.HuaWeiInventory;
import com.huawei.inventory.PhyLinkMgr;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import com.huawei.vrf.Vrf;
import com.huawei.vrf.VrfDeviceProvider;
import com.huawei.vrf.VrfMgr;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wjl.provider.ProviderException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class VirBgpViewTest {
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
    public void bgpPeerBetweenVrf() throws ProviderException {
        VrfDeviceProvider provider = new VrfDeviceProvider();
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "test");
        inputs.put("host", "AG01");
        String id1 = provider.create("1", inputs);
        inputs.put("host", "AG02");
        String id2 = provider.create("2", inputs);

        PhyRouter ag01 = PhyRouterMgr.getRouter("AG01");
        PhyRouter ag02 = PhyRouterMgr.getRouter("AG02");
        Vrf vrf1 = VrfMgr.getVrf(id1);
        Vrf vrf2 = VrfMgr.getVrf(id2);

        VirRouterView view1 = new VirRouterView(VrfMgr.getVrf(id1));
        VirRouterView view2 = new VirRouterView(VrfMgr.getVrf(id2));

        VirBgpView bgp1 = (VirBgpView)view1.cfgBgp();
        VirBgpView bgp2 = (VirBgpView)view2.cfgBgp();
        bgp1.setRouterId("244.244.1.1");
        bgp2.setRouterId("244.244.1.2");
        bgp1.addPeer("244.244.1.2", ag02.getAsNumber());
        bgp2.addPeer("244.244.1.1", ag01.getAsNumber());

        assertTrue(ag01.checkConfig(CLI.BGP, ag01.getAsNumber()));
        assertTrue(ag01.checkConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, "test"));
        assertTrue(ag01.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, vrf1.getRtForBgpPeer(), CLI.EXPORT));
        assertTrue(ag01.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, vrf2.getRtForBgpPeer(), CLI.IMPORT));

        assertTrue(ag02.checkConfig(CLI.BGP, ag02.getAsNumber()));
        assertTrue(ag02.checkConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, "test"));
        assertTrue(ag02.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, vrf2.getRtForBgpPeer(), CLI.EXPORT));
        assertTrue(ag02.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, vrf1.getRtForBgpPeer(), CLI.IMPORT));
    }
}