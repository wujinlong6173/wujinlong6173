package wjl.provider;

import java.util.*;

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
            registerDeviceProviders.put(dev.getName(), dev);
        }
        for (LinkProvider link : linkProviders) {
            registerLinkProviders.put(link.getName(), link);
        }
    }

    private static class LazyProviderLoader {
        private static ProviderLoader INSTANCE = new ProviderLoader();
    }

    /**
     * 获取所有设备供应商的名称
     *
     * @return 名称集合
     */
    public static Set<String> listDeviceProviders() {
        return LazyProviderLoader.INSTANCE.registerDeviceProviders.keySet();
    }

    /**
     * 获取所有链路供应商的名称
     *
     * @return 名称集合
     */
    public static Set<String> listLinkProviders() {
        return LazyProviderLoader.INSTANCE.registerLinkProviders.keySet();
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
