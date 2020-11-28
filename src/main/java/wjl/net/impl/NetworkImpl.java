package wjl.net.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络意图背后的实现，即设备实现和链路实现的集合。
 */
public class NetworkImpl {
    private final Map<String, DeviceImpl> implDevices = new ConcurrentHashMap<>();
    private final Map<String, LinkImpl> implLinks = new ConcurrentHashMap<>();

    public void addDeviceImpl(DeviceImpl impl) {
        implDevices.put(impl.getDevId(), impl);
    }

    /**
     * 根据设备意图查找实现
     * 
     * @param intentId 设备意图的标识
     * @return 设备实现
     */
    public DeviceImpl getDeviceImpl(String intentId) {
        return implDevices.get(intentId);
    }

    /**
     * 删除设备意图的实现
     * @param intentId
     */
    public void delDeviceImpl(String intentId) {
        implDevices.remove(intentId);
    }
    
    public void addLinkImpl(LinkImpl impl) {
        implLinks.put(impl.getLinkId(), impl);
    }

    /**
     * 根据链路意图查找实现
     * 
     * @param intentId 链路意图的标识
     * @return 链路实现
     */
    public LinkImpl getLinkImpl(String intentId) {
        return implLinks.get(intentId);
    }

    public void delLinkImpl(String intentId) {
        implLinks.remove(intentId);
    }
}
