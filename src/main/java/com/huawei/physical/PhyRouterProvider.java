package com.huawei.physical;

import wjl.datamodel.SchemaParser;
import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.DeviceProvider;
import wjl.provider.ProviderException;

import java.util.Map;

/**
 * 模拟物理路由器的供应商。
 */
public class PhyRouterProvider implements DeviceProvider {
    private final ObjectSchema createSchema;
    private final ObjectSchema configSchema;

    public PhyRouterProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("PhysicalRouter", "properties:\n" +
                "name: {type: string, required: true, flag: CR}\n");
        configSchema = null;
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return createSchema;
    }

    @Override
    public ObjectSchema getConfigSchema() {
        return configSchema;
    }

    @Override
    public String create(String idInNms, Map<String, Object> inputs) throws ProviderException {
        // 物理路由器的名称也是唯一的
        String name = (String)inputs.get("name");
        PhyRouter pr = new PhyRouter();
        pr.setIdInNms(idInNms);
        pr.setName(name);
        PhyRouterMgr.addRouter(pr);
        return name;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        PhyRouterMgr.delRouter(idInProvider);
    }
}
