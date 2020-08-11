package wjl.provider;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProviderLoaderTest {
    @Test
    public void notFound() {
        assertNull(ProviderLoader.getDeviceProvider("wjl.net.UndefinedProvider"));
        assertNull(ProviderLoader.getLinkProvider("wjl.net.UndefinedProvider"));
    }

    @Test
    public void huaweiProvider() {
        assertNotNull(ProviderLoader.getDeviceProvider("wjl.provider.DummyDeviceProvider"));
        assertNotNull(ProviderLoader.getLinkProvider("wjl.provider.DummyLinkProvider"));
    }
}