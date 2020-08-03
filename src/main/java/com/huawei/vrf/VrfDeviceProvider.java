package com.huawei.vrf;

import com.huawei.schema.SchemaParser;
import wjl.net.provider.DeviceProvider;
import wjl.net.schema.ObjectSchema;

import java.util.Map;
import java.util.UUID;

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
    public String getName() {
        return this.getClass().getName();
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
    public String create(String idInNms, Map<String, Object> inputs) {
        Vrf vrf = new Vrf();
        vrf.setId(UUID.randomUUID().toString());
        vrf.setHost((String)inputs.get("host"));
        vrf.setName((String)inputs.get("name"));
        VrfMgr.createVrf(vrf);
        return vrf.getId();
    }
}
