package wjl.provider;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ProductProviderMgrTest {
    @Test
    public void testAddGet() {
        DeviceProvider p1 = new DemoDeviceProvider("test", "dog");
        DeviceProvider p2 = new DemoDeviceProvider("test", "cat");
        LinkProvider p3 = new DemoLinkProvider("demo", "p2p");
        LinkProvider p4 = new DemoLinkProvider("demo", "bc");

        ProductProviderMgr mgr = new ProductProviderMgr();
        mgr.addDeviceProvider(p1);
        mgr.addDeviceProvider(p2);
        mgr.addLinkProvider(p3);
        mgr.addLinkProvider(p4);

        assertSame(p1, mgr.getDeviceProvider("test-dog"));
        assertSame(p2, mgr.getDeviceProvider("test-cat"));
        assertSame(p3, mgr.getLinkProvider("demo-p2p"));
        assertSame(p4, mgr.getLinkProvider("demo-bc"));
        assertSame(p1, mgr.getDeviceProvider("test", "dog"));
        assertSame(p3, mgr.getLinkProvider("demo", "p2p"));
    }
}