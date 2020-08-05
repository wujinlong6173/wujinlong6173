package wjl.net.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备意图背后的实现，映射到供应商的设备对象。
 */
public class DeviceImpl {
    private final String devId;
    private final String outerId;
    private final String provider;
    private final Map<String,Object> inputs;

    public DeviceImpl(String devId, String outerId, String provider, Map<String,Object> inputs) {
        this.devId = devId;
        this.outerId = outerId;
        this.provider = provider;
        this.inputs = inputs;
    }

    public String getDevId() {
        return devId;
    }

    public String getOuterId() {
        return outerId;
    }

    public String getProvider() {
        return provider;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }
}
