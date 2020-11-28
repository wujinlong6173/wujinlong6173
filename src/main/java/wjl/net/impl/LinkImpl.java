package wjl.net.impl;

import java.util.Map;

/**
 * 链路意图背后的实现，映射到供应商的链路对象。
 */
public class LinkImpl {
    private final String linkId;
    private final String outerId;
    private final Map<String,Object> inputs;
    private final String provider;
    private final String product;

    public LinkImpl(String linkId, String outerId, Map<String,Object> inputs,
                    String provider, String product) {
        this.linkId = linkId;
        this.outerId = outerId;
        this.inputs = inputs;
        this.provider = provider;
        this.product = product;
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

    public String getProduct() {
        return product;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }
}
