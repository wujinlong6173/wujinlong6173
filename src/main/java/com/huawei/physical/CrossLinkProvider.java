package com.huawei.physical;

import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.AbsProductProvider;
import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;

import java.util.Map;

/**
 * 创建跨运营商的链路。
 */
public class CrossLinkProvider extends AbsProductProvider implements LinkProvider {
    public CrossLinkProvider(String providerName, String productName) {
        super(providerName, productName);
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }

    @Override
    public String create(String idInNms,
                         String srcOuterId, String srcPortName, String srcProvider,
                         String dstOuterId, String dstPortName, String dstProvider,
                         Map<String, Object> inputs) throws ProviderException {
        return null;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {

    }
}
