package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

import java.util.Map;

public class DemoDeviceProvider extends AbsProductProvider implements DeviceProvider {
    public DemoDeviceProvider(String providerName, String productName) {
        super(providerName, productName);
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }

    @Override
    public ObjectSchema getConfigSchema() {
        return null;
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String create(String idInNms, Map<String, Object> inputs) {
        return idInNms;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {

    }
}
