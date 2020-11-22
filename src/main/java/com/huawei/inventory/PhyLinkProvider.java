package com.huawei.inventory;

import wjl.datamodel.SchemaParser;
import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;
import wjl.util.ErrorType;

import java.util.Locale;
import java.util.Map;

/**
 * 模拟物理链路的供应商。
 */
public class PhyLinkProvider implements LinkProvider {
    private final ObjectSchema createSchema;

    public PhyLinkProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("PhysicalLink", "properties: {}");
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return createSchema;
    }

    @Override
    public String create(String idInNms,
            String srcRouterId, String srcPortName, String srcProvider,
            String dstRouterId, String dstPortName, String dstProvider,
            Map<String, Object> inputs) throws ProviderException {
        PhyRouter srcRouter = PhyRouterMgr.getRouter(srcRouterId);
        PhyRouter dstRouter = PhyRouterMgr.getRouter(dstRouterId);
        if (srcRouter == null || dstRouter == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format(Locale.ENGLISH, "physical router %s or %s don't exist.",
                            srcRouterId, dstRouterId));
        }

        // 简化设计，让idInNms等于idInProvider
        PhyLink pl = new PhyLink();
        pl.setId(idInNms);
        pl.setSrcDevice(srcRouter.getName());
        pl.setSrcPort(srcPortName);
        pl.setDstDevice(dstRouter.getName());
        pl.setDstPort(dstPortName);
        PhyLinkMgr.addLink(pl);
        return idInNms;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        PhyLinkMgr.delLink(idInProvider);
    }
}
