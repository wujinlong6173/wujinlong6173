package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

import java.util.Map;
import java.util.UUID;

/**
 * 虚无的设备供应商
 */
public class DummyDeviceProvider implements DeviceProvider {
    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }

    @Override
    public ObjectSchema getConfigSchema() {
        return null;
    }

    @Override
    public String create(String idInNms, Map<String, Object> inputs) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {

    }
}
