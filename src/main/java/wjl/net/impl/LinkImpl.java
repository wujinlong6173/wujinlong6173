package wjl.net.impl;

import java.util.Map;

/**
 * 链路意图背后的实现，映射到供应商的链路对象。
 */
public class LinkImpl {
    private final String linkId;
    private final String outerId;
    private final String provider;
    private final Map<String,Object> inputs;

    public LinkImpl(String linkId, String outerId, String provider, Map<String,Object> inputs) {
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
