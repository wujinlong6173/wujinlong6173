package com.huawei.physical;

import wjl.datamodel.SchemaParser;
import wjl.datamodel.schema.ObjectSchema;
import wjl.provider.AbsProductProvider;
import wjl.provider.PortProvider;
import wjl.provider.ProviderException;
import wjl.util.ErrorType;

import java.util.Locale;
import java.util.Map;

/**
 * 不同运营商互联场景，运营商将部分路由器开放给互联网络。
 * 互联公司在界面看到的是自己给设备、端口取得名称，实际引用运营商的物理设备和端口。
 * 通过执行部署操作，调用本接口，建立引用关系。
 */
public class RefPortProvider extends AbsProductProvider implements PortProvider {
    private final ObjectSchema createSchema;

    /**
     * @param providerName 供应商的名称
     * @param productName  产品的名称
     */
    public RefPortProvider(String providerName, String productName) {
        super(providerName, productName);
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("ReferencePort",
                "required: true\n" +
                      "properties:\n" +
                      "  refPort: {type: string, required: true, flag: CR}\n");
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return createSchema;
    }

    @Override
    public String create(String routerName, String logicPortName, Map<String, Object> inputs) throws ProviderException {
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(routerName);
        if (pr == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format(Locale.ENGLISH, "router %s is not exist.", routerName));
        }

        PhyLinkMgr linkMgr = getInstance(PhyLinkMgr.class);
        String refPort = (String)inputs.get("refPort");
        if (linkMgr.findLinkOnPort(routerName, refPort) != null) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format(Locale.ENGLISH, "port %s-%s is occupied.", routerName, refPort));
        }

        return null;
    }

    @Override
    public void delete(String deviceId, String portId) throws ProviderException {

    }

}
