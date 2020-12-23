package wjl.net;

import org.junit.Test;
import wjl.provider.DemoDeviceProvider;
import wjl.provider.DemoLinkProvider;
import wjl.provider.ProductProviderMgr;
import wjl.provider.ProviderException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NetworkApiTest {

    @Test
    public void testNetworkIntent() throws NetworkException {
        NetworkApi api = new NetworkApi(null);
        String r1 = api.createDevice("Router1");
        String r2 = api.createDevice("Router2");
        String p1 = api.createPort(r1, "Eth1", null);
        String p2 = api.createPort(r2, "Eth2", null);
        String lk1 = api.createLink("Link1", p1, p2);
        NetworkSummary sum1 = api.summary();
        assertEquals(2, sum1.getDevice());
        assertEquals(2, sum1.getPort());
        assertEquals(1, sum1.getLink());

        api.deleteLink(lk1);
        api.deleteDevice(r1);
        api.deleteDevice(r2);
        NetworkSummary sum2 = api.summary();
        assertEquals(0, sum2.getDevice());
        assertEquals(0, sum2.getPort());
        assertEquals(0, sum2.getLink());
    }

    @Test
    public void testModifyName() throws NetworkException, ProviderException {
        ProductProviderMgr productMgr = new ProductProviderMgr();
        productMgr.addDeviceProvider(new DemoDeviceProvider("test", "demo"));
        productMgr.addLinkProvider(new DemoLinkProvider("test", "demo"));

        NetworkApi api = new NetworkApi(productMgr);
        String r1 = api.createDevice("Router1");
        String r2 = api.createDevice("Router2");
        String p1 = api.createPort(r1, "Eth1", null);
        String p2 = api.createPort(r2, "Eth2", null);
        String lk1 = api.createLink("Link1", p1, p2);

        // 修改不存在的对象名称
        assertTrue(api.modifyDeviceName(null, "test"));
        assertTrue(api.modifyPortName(null, "test"));
        assertTrue(api.modifyLinkName(null, "test"));

        // 修改未部署的对象名称
        assertTrue(api.modifyDeviceName(r1, "NewRouter1"));
        assertTrue(api.modifyPortName(p1, "NewEth1"));
        assertTrue(api.modifyLinkName(lk1, "NewLink1"));

        // 不能修改已部署的对象名称
        Map<String,Object> inputs = new HashMap<>();
        api.deployDevice(r1, "test-demo", inputs);
        api.deployDevice(r2, "test-demo", inputs);
        api.deployLink(lk1, "test-demo", inputs);
        assertFalse(api.modifyDeviceName(r2, "NewRouter2"));
        assertFalse(api.modifyPortName(p2, "NewEth2"));
        assertFalse(api.modifyLinkName(lk1, "NewLink2"));
    }
}