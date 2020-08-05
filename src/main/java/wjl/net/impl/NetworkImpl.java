package wjl.net.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络意图背后的实现，即设备实现和链路实现的集合。
 */
public class NetworkImpl {
    private static Map<String, DeviceImpl> implDevices = new ConcurrentHashMap<>();
    private static Map<String, LinkImpl> implLinks = new ConcurrentHashMap<>();

    public static void addDeviceImpl(DeviceImpl impl) {
        implDevices.put(impl.getDevId(), impl);
    }

    /**
     * 根据设备意图查找实现
     * 
     * @param intentId 设备意图的标识
     * @return 设备实现
     */
    public static DeviceImpl getDeviceImpl(String intentId) {
        return implDevices.get(intentId);
    }

    public static void addLinkImpl(LinkImpl impl) {
        implLinks.put(impl.getLinkId(), impl);
    }

    /**
     * 根据链路意图查找实现
     * 
     * @param intentId 链路意图的标识
     * @return 链路实现
     */
    public static LinkImpl getLinkImpl(String intentId) {
        return implLinks.get(intentId);
    }
}
