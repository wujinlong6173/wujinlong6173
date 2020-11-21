package wjl.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 管理一张网络选中的所有供应商，本对象可以被多张网络共享。
 */
public class ProviderMgr {
    private final Map<String, DeviceProvider> deviceProviders = new HashMap<>();
    private final Map<String, LinkProvider> linkProviders = new HashMap<>();

    public void addDeviceProvider(String name, DeviceProvider provider) {
        deviceProviders.put(name, provider);
    }

    public DeviceProvider getDeviceProvider(String name) {
        return deviceProviders.get(name);
    }

    public Set<String> listDeviceProviders() {
        return deviceProviders.keySet();
    }

    public void addLinkProvider(String name, LinkProvider provider) {
        linkProviders.put(name, provider);
    }

    public LinkProvider getLinkProvider(String name) {
        return linkProviders.get(name);
    }

    public Set<String> listLinkProviders() {
        return linkProviders.keySet();
    }
}
