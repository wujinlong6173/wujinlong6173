package com.huawei.vlan;

import com.huawei.common.CLI;
import com.huawei.common.InterfaceMgr;
import com.huawei.inventory.PhyLink;
import com.huawei.inventory.PhyLinkMgr;
import com.huawei.inventory.PhyRouter;
import com.huawei.inventory.PhyRouterMgr;
import wjl.datamodel.SchemaParser;
import com.huawei.vrf.VrfMgr;

import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;
import wjl.datamodel.schema.ObjectSchema;
import wjl.util.ErrorType;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class VLanLinkProvider implements LinkProvider {
    private final ObjectSchema createSchema;

    public VLanLinkProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("HW.VLan", "properties: {}");
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
    public String create(String idInNms, 
            String srcVrfId, String srcPortName, String srcProvider,  
            String dstVrfId, String dstPortName, String dstProvider, 
            Map<String, Object> inputs) throws ProviderException {
        
        String srcHost = VrfMgr.getHostOfVrf(srcVrfId);
        String dstHost = VrfMgr.getHostOfVrf(dstVrfId);
        if (srcHost == null || dstHost == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR, 
                    String.format(Locale.ENGLISH, "virtual router %s or %s don't exist.",
                            srcVrfId, dstVrfId));
        }
        
        PhyLink phyLink = PhyLinkMgr.findLinkBetweenDevice(srcHost, dstHost);
        if (phyLink == null) {
            throw new ProviderException(ErrorType.NO_USABLE_RESOURCE,
                    String.format(Locale.ENGLISH, "no usable link between %s and %s.",
                            srcHost, dstHost));
        }
        
        int vLanId = phyLink.allocVLanId();

        VLanSubIf srcIf = createVLanSubIf(srcHost, phyLink.getPortOfLink(srcHost), vLanId);
        VLanSubIf dstIf = createVLanSubIf(dstHost, phyLink.getPortOfLink(dstHost), vLanId);

        VrfMgr.bindInterface(srcVrfId, srcPortName, srcIf);
        VrfMgr.bindInterface(dstVrfId, dstPortName, dstIf);

        VLanLink lk = new VLanLink();
        lk.setId(UUID.randomUUID().toString());
        lk.setSrcSubIf(srcIf.getId());
        lk.setDstSubIf(dstIf.getId());
        VLanDao.addVLanLink(lk);
        return lk.getId();
    }

    private VLanSubIf createVLanSubIf(String host, String port, int vLanId) {
        VLanSubIf inf = new VLanSubIf();
        inf.setId(UUID.randomUUID().toString());
        inf.setHost(host);
        inf.setPort(port);
        inf.setVlanId(vLanId);

        InterfaceMgr.addInterface(inf);
        PhyRouter pr = PhyRouterMgr.getRouter(host);
        if (pr != null) {
            pr.addConfig(CLI.INTERFACE, inf.getInterfaceName());
            pr.addConfig(CLI.__, "encapsulation", "dot1q", String.valueOf(vLanId));
        }

        return inf;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        VLanLink lk = VLanDao.getVLanLink(idInProvider);
        if (lk == null) {
            return;
        }

        VLanSubIf srcIf = (VLanSubIf)InterfaceMgr.getInterface(lk.getSrcSubIf());
        VLanSubIf dstIf = (VLanSubIf)InterfaceMgr.getInterface(lk.getDstSubIf());
        VrfMgr.unBindInterface(srcIf);
        VrfMgr.unBindInterface(dstIf);

        VLanDao.delVLanLink(idInProvider);
        deleteVLanSubIf(srcIf);
        deleteVLanSubIf(dstIf);
    }

    private void deleteVLanSubIf(VLanSubIf inf) {
        InterfaceMgr.delInterface(inf.getId());

        PhyRouter pr = PhyRouterMgr.getRouter(inf.getHost());
        if (pr != null) {
            pr.undoConfig(CLI.INTERFACE, inf.getInterfaceName());
        }
    }
}
