package com.huawei.vrf;

import com.huawei.common.CLI;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import wjl.datamodel.SchemaParser;
import wjl.provider.DeviceProvider;
import wjl.provider.ProviderException;
import wjl.datamodel.schema.ObjectSchema;
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
        String name = (String)inputs.get("name");
        PhyRouter pr = PhyRouterMgr.getRouter(host);
        if (pr == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR,
                    String.format("host %s does not exist.", host));
        }

        pr.addConfig(CLI.IP, CLI.VPN_INSTANCE, name);
        pr.addConfig(CLI.__, "address-family", "ipv4", "unicast");

        Vrf vrf = new Vrf();
        vrf.setId(UUID.randomUUID().toString());
        vrf.setIdInNms(idInNms);
        vrf.setHost(host);
        vrf.setName(name);
        VrfMgr.createVrf(vrf);
        return vrf.getId();
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        Vrf vrf = VrfMgr.getVrf(idInProvider);
        if (vrf == null) {
            return;
        }

        PhyRouter pr = PhyRouterMgr.getRouter(vrf.getHost());
        pr.undoConfig(CLI.IP, CLI.VPN_INSTANCE, vrf.getName());

        VrfMgr.deleteVrf(idInProvider);
    }
}
