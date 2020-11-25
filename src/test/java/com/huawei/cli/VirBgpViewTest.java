package com.huawei.cli;

import com.huawei.common.CLI;
import com.huawei.physical.PhyLinkMgr;
import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import com.huawei.physical.PhyRouterProvider;
import com.huawei.vrf.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wjl.provider.ProviderException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class VirBgpViewTest {
    @Before
    public void init() throws ProviderException {
        PhyRouterProvider routerProvider = new PhyRouterProvider();
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "BgpTest1");
        routerProvider.create(UUID.randomUUID().toString(), inputs);
        inputs.put("name", "BgpTest2");
        routerProvider.create(UUID.randomUUID().toString(), inputs);
    }

    @After
    public void fini() {
        PhyDeviceMgr.clear();
        PhyLinkMgr.clear();
    }

    @Test
    public void bgpPeerBetweenVrf() throws ProviderException {
        VrfDeviceProvider provider = new VrfDeviceProvider();
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "test");
        inputs.put("host", "BgpTest1");
        String id1 = provider.create("1", inputs);
        inputs.put("host", "BgpTest2");
        String id2 = provider.create("2", inputs);

        PhyRouter ag01 = PhyDeviceMgr.getRouter("BgpTest1");
        PhyRouter ag02 = PhyDeviceMgr.getRouter("BgpTest2");
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

    @Test
    public void bgpPeerGroup() throws ProviderException {
        VrfDeviceProvider provider = new VrfDeviceProvider();
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "test");
        inputs.put("host", "BgpTest1");
        String id3 = provider.create("3", inputs);
        inputs.put("host", "BgpTest2");
        String id4 = provider.create("4", inputs);

        PhyRouter ag01 = PhyDeviceMgr.getRouter("BgpTest1");
        PhyRouter ag02 = PhyDeviceMgr.getRouter("BgpTest2");

        VirRouterView view3 = new VirRouterView(VrfMgr.getVrf(id3));
        VirRouterView view4 = new VirRouterView(VrfMgr.getVrf(id4));

        VirBgpView bgp3 = (VirBgpView)view3.cfgBgp();
        VirBgpView bgp4 = (VirBgpView)view4.cfgBgp();
        bgp3.joinPeerGroupAsHub("group1");
        bgp4.joinPeerGroupAsSpoke("group1");
        BgpPeerGroupImpl group = VpnRes.getOrCreatePeerGroup(ag01.getAsNumber(), "group1");

        assertTrue(ag01.checkConfig(CLI.BGP, ag01.getAsNumber()));
        assertTrue(ag01.checkConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, "test"));
        assertTrue(ag01.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getHubRT(), CLI.EXPORT));
        assertTrue(ag01.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getHubRT(), CLI.IMPORT));
        assertTrue(ag01.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getSpokeRT(), CLI.IMPORT));

        assertTrue(ag02.checkConfig(CLI.BGP, ag02.getAsNumber()));
        assertTrue(ag02.checkConfig(CLI.__, CLI.IPV4_FAMILY, CLI.VPN_INSTANCE, "test"));
        assertTrue(ag02.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getSpokeRT(), CLI.EXPORT));
        assertTrue(ag02.checkConfig(CLI.__, CLI.__, CLI.VPN_TARGET, group.getHubRT(), CLI.IMPORT));
    }
}