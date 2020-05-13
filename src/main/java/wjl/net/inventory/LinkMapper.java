package wjl.net.inventory;

import java.util.Map;

/**
 * 保持每条链路对应的提供者信息。
 */
public class LinkMapper {
    private final String linkId;
    private final String outerId;
    private final String provider;
    private final Map<String,Object> inputs;

    public LinkMapper(String linkId, String outerId, String provider, Map<String,Object> inputs) {
        this.linkId = linkId;
        this.outerId = outerId;
        this.provider = provider;
        this.inputs = inputs;
    }

    public String getLinkId() {
        return linkId;
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
