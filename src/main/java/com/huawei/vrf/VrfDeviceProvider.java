package com.huawei.vrf;

import com.huawei.common.CLI;
import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import wjl.datamodel.SchemaParser;
import wjl.docker.AbstractMember;
import wjl.provider.DeviceProvider;
import wjl.provider.ProviderException;
import wjl.datamodel.schema.ObjectSchema;
import wjl.util.ErrorType;

import java.util.Map;
import java.util.UUID;

public class VrfDeviceProvider extends AbstractMember implements DeviceProvider {
    private final ObjectSchema createSchema;
    private final ObjectSchema configSchema;

    public VrfDeviceProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("HW.VRF", "properties:\n" +
                "  name: {type: string, required: true, flag: CR}\n" +
                "  host: {type: string, required: true, flag: CR}\n");
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
    public String getIcon() {
        return "/images/router-deploy.png;";
    }

    @Override
    public String create(String idInNms, Map<String, Object> inputs) throws ProviderException {
        String host = (String)inputs.get("host");
        String name = (String)inputs.get("name");
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(host);
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
        VrfMgr vrfMgr = getInstance(VrfMgr.class);
        vrfMgr.createVrf(vrf);
        return vrf.getId();
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {
        VrfMgr vrfMgr = getInstance(VrfMgr.class);
        Vrf vrf = vrfMgr.getVrf(idInProvider);
        if (vrf == null) {
            return;
        }

        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        PhyRouter pr = deviceMgr.getRouter(vrf.getHost());
        pr.undoConfig(CLI.IP, CLI.VPN_INSTANCE, vrf.getName());

        vrfMgr.deleteVrf(idInProvider);
    }
}
