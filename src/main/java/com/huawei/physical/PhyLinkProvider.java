package com.huawei.physical;

import wjl.datamodel.SchemaParser;
import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.AbsProductProvider;
import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;
import wjl.util.ErrorType;

import java.util.Locale;
import java.util.Map;

/**
 * 模拟物理链路的供应商，物理路由器和交换机可以互联。
 */
public class PhyLinkProvider extends AbsProductProvider implements LinkProvider {
    private final ObjectSchema createSchema;

    public PhyLinkProvider(String providerName, String productName) {
        super(providerName, productName);
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("PhysicalLink", "properties: {}");
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return createSchema;
    }

    @Override
    public String create(String idInNms,
            String srcDeviceId, String srcPortName, String srcProvider,
            String dstDeviceId, String dstPortName, String dstProvider,
            Map<String, Object> inputs) throws ProviderException {
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyDevice srcDev = deviceMgr.getDevice(srcDeviceId);
        PhyDevice dstDev = deviceMgr.getDevice(dstDeviceId);
        if (srcDev == null || dstDev == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format(Locale.ENGLISH, "physical router %s or %s don't exist.",
                            srcDeviceId, dstDeviceId));
        }

        // 简化设计，让idInNms等于idInProvider
        PhyLink pl = new PhyLink();
        pl.setId(idInNms);
        pl.setSrcDevice(srcDev.getName());
        pl.setSrcPort(srcPortName);
        pl.setDstDevice(dstDev.getName());
        pl.setDstPort(dstPortName);
        PhyLinkMgr linkMgr = getInstance(PhyLinkMgr.class);
        linkMgr.addLink(pl);
        return idInNms;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {
        PhyLinkMgr linkMgr = getInstance(PhyLinkMgr.class);
        linkMgr.delLink(idInProvider);
    }
}
