package com.huawei.physical;

import wjl.datamodel.SchemaParser;
import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.AbsProductProvider;
import wjl.provider.DeviceProvider;
import wjl.provider.ProviderException;
import wjl.util.ErrorType;

import java.util.Map;

/**
 * 不同运营商互联场景，运营商将部分路由器开放给互联网络。
 * 互联网络有自己的网络意图，先创建设备意图，然后引用运营商的物理设备。
 * 通过执行部署操作，调用本接口，建立引用关系。
 */
public class RefRouterProvider extends AbsProductProvider implements DeviceProvider {
    private final ObjectSchema createSchema;

    public RefRouterProvider(String providerName, String productName) {
        super(providerName, productName);
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("ReferenceRouter",
           "required: true\n" +
                 "properties:\n" +
                 "  refRouterName: {type: string, required: true, flag: CR}\n");
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return createSchema;
    }

    @Override
    public ObjectSchema getConfigSchema() {
        return null;
    }

    @Override
    public String getIcon() {
        return "/images/router_ref.png;";
    }

    @Override
    public String create(String idInNms, Map<String, Object> inputs) throws ProviderException {
        String refRouterName = (String)inputs.get("refRouterName");
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(refRouterName);
        if (pr == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format("can't reference router %s.", refRouterName));
        }
        return refRouterName;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {

    }
}
