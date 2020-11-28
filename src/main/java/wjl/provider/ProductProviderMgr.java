package wjl.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import wjl.datamodel.schema.ObjectSchema;
import wjl.util.YamlLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理一张网络选中的所有供应商，本对象可以被多张网络共享。
 */
public class ProductProviderMgr {
    // 键值是供应商的名称加产品的名称
    private final Map<String, DeviceProvider> deviceProviders = new HashMap<>();
    private final Map<String, LinkProvider> linkProviders = new HashMap<>();

    public void addDeviceProvider(DeviceProvider provider) {
        String name = String.format("%s-%s", provider.getProviderName(), provider.getProductName());
        deviceProviders.put(name, provider);
    }

    public DeviceProvider getDeviceProvider(String productName) {
        return deviceProviders.get(productName);
    }

    public void addLinkProvider(LinkProvider provider) {
        String name = String.format("%s-%s", provider.getProviderName(), provider.getProductName());
        linkProviders.put(name, provider);
    }

    public LinkProvider getLinkProvider(String productName) {
        return linkProviders.get(productName);
    }

    /**
     *
     * @return 键值为供应商的名称，值为供应商特有参数的说明
     */
    public Map<String, String> listDeviceProviders() {
        Map<String, String> nameToSchema = new HashMap<>();
        for (Map.Entry<String, DeviceProvider> entry : deviceProviders.entrySet()) {
            nameToSchema.put(entry.getKey(), strObjectSchema(entry.getValue().getCreateSchema()));
        }
        return nameToSchema;
    }

    public Map<String, String> listLinkProviders() {
        Map<String, String> nameToSchema = new HashMap<>();
        for (Map.Entry<String, LinkProvider> entry : linkProviders.entrySet()) {
            nameToSchema.put(entry.getKey(), strObjectSchema(entry.getValue().getCreateSchema()));
        }
        return nameToSchema;
    }

    private static String strObjectSchema(ObjectSchema schema) {
        if (schema == null) {
            return "";
        }

        try {
            return YamlLoader.obj2str(schema.serialize());
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
