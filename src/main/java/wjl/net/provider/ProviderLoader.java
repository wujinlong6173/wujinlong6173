package wjl.net.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 加载设备和链路提供商，所有提供商必须在META-INF.services目录中注册。
 */
public class ProviderLoader {
    private final Map<String, DeviceProvider> registerDeviceProviders;
    private final Map<String, LinkProvider> registerLinkProviders;

    private ProviderLoader() {
        ServiceLoader<DeviceProvider> deviceProviders = ServiceLoader.load(DeviceProvider.class);
        ServiceLoader<LinkProvider> linkProviders = ServiceLoader.load(LinkProvider.class);
        registerDeviceProviders = new HashMap<>();
        registerLinkProviders = new HashMap<>();
        for (DeviceProvider dev : deviceProviders) {
            registerDeviceProviders.put(dev.getClass().getName(), dev);
        }
        for (LinkProvider link : linkProviders) {
            registerLinkProviders.put(link.getClass().getName(), link);
        }
    }

    private static class LazyProviderLoader {
        private static ProviderLoader INSTANCE = new ProviderLoader();
    }

    /**
     * 根据类名称查找设备提供商
     *
     * @param name 类名称
     * @return 找不到时返回空
     */
    public static DeviceProvider getDeviceProvider(String name) {
        return LazyProviderLoader.INSTANCE.registerDeviceProviders.get(name);
    }

    /**
     * 根据类名称查找链路提供商
     *
     * @param name 类名称
     * @return 找不到时返回空
     */
    public static LinkProvider getLinkProvider(String name) {
        return LazyProviderLoader.INSTANCE.registerLinkProviders.get(name);
    }
}
