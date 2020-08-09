package com.huawei.vrf;

import com.huawei.inventory.LinkMgr;
import com.huawei.schema.SchemaParser;
import wjl.net.provider.DeviceProvider;
import wjl.net.provider.ProviderException;
import wjl.net.schema.ObjectSchema;
import wjl.util.ErrorType;

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
    public String create(String idInNms, Map<String, Object> inputs) throws ProviderException {
        String host = (String)inputs.get("host");
        if (!LinkMgr.isDeviceExist(host)) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format("host %s does not exist.", host));
        }

        Vrf vrf = new Vrf();
        vrf.setId(UUID.randomUUID().toString());
        vrf.setHost(host);
        vrf.setName((String)inputs.get("name"));
        VrfMgr.createVrf(vrf);
        return vrf.getId();
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        VrfMgr.deleteVrf(idInProvider);
    }
}
