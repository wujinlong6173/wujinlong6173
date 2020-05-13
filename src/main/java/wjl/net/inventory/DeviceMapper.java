package wjl.net.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保持每个设备对应的提供者信息。
 */
public class DeviceMapper {
    private final String devId;
    private final String outerId;
    private final String provider;
    private final Map<String,Object> inputs;

    public DeviceMapper(String devId, String outerId, String provider, Map<String,Object> inputs) {
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
