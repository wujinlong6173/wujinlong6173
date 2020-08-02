package wjl.net.provider;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProviderLoaderTest {
    @Test
    public void notFound() {
        assertNull(ProviderLoader.getDeviceProvider("wjl.net.DummyProvider"));
        assertNull(ProviderLoader.getLinkProvider("wjl.net.DummyProvider"));
    }

    @Test
    public void huaweiProvider() {
        assertNotNull(ProviderLoader.getDeviceProvider("com.huawei.vrf.VrfDeviceProvider"));
        assertNotNull(ProviderLoader.getLinkProvider("com.huawei.vlan.VLanLinkProvider"));
    }
}