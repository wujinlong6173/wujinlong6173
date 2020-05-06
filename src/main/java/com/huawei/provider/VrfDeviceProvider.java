package com.huawei.provider;

import com.huawei.schema.SchemaParser;
import wjl.net.provider.DeviceProvider;
import wjl.net.schema.ObjectSchema;

public class VrfDeviceProvider implements DeviceProvider {
    private final ObjectSchema createSchema;
    private final ObjectSchema configSchema;

    public VrfDeviceProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("HW.VRF", "properties:\n" +
                "name: {type: string, required: true, flag: CR}\n" +
                "host: {type: string, required: true, flag: CR}\n");
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
}
