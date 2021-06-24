package wjl.net.impl;

import java.util.Map;

/**
 * 设备意图背后的实现，映射到供应商的设备对象。
 */
public class DeviceImpl {
    private final String devId;
    private final String outerId;
    private final Map<String,Object> inputs;
    private final String provider;
    private final String product;

    public DeviceImpl(String devId, String outerId, Map<String,Object> inputs,
                      String provider, String product) {
        this.devId = devId;
        this.outerId = outerId;
        this.inputs = inputs;
        this.product = product;
        this.provider = provider;
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

    public String getProduct() {
        return product;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }
}
