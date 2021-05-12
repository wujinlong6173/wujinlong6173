package com.huawei.vlan;

import com.huawei.common.CLI;
import com.huawei.common.InterfaceMgr;
import com.huawei.physical.PhyLink;
import com.huawei.physical.PhyLinkMgr;
import com.huawei.physical.PhyRouter;
import com.huawei.physical.PhyDeviceMgr;
import wjl.cli.ConfigHolder;
import wjl.datamodel.SchemaParser;
import com.huawei.vrf.VrfMgr;

import wjl.provider.AbsProductProvider;
import wjl.provider.LinkProvider;
import wjl.provider.ProviderException;
import wjl.datamodel.schema.ObjectSchema;
import wjl.util.ErrorType;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class VLanLinkProvider extends AbsProductProvider implements LinkProvider {
    private final ObjectSchema createSchema;

    public VLanLinkProvider(String providerName, String productName) {
        super(providerName, productName);
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("HW.VLan", "properties: {}");
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
        // srcVrfId和dstVrfId是VrfDeviceProvider.create的返回值
        VrfMgr vrfMgr = getInstance(VrfMgr.class);
        PhyLinkMgr linkMgr = getInstance(PhyLinkMgr.class);
        VLanDao vLanDao = getInstance(VLanDao.class);

        String srcHost = vrfMgr.getHostOfVrf(srcVrfId);
        String dstHost = vrfMgr.getHostOfVrf(dstVrfId);
        if (srcHost == null || dstHost == null) {
            throw new ProviderException(ErrorType.INPUT_ERROR, 
                    String.format(Locale.ENGLISH, "virtual router %s or %s don't exist.",
                            srcVrfId, dstVrfId));
        }

        PhyLink phyLink = linkMgr.findLinkBetweenDevice(srcHost, dstHost);
        if (phyLink == null) {
            throw new ProviderException(ErrorType.NO_USABLE_RESOURCE,
                    String.format(Locale.ENGLISH, "no usable link between %s and %s.",
                            srcHost, dstHost));
        }
        
        int vLanId = phyLink.allocVLanId();

        // 将逻辑端口的名称记在实际端口的备注上
        VLanSubIf srcIf = createVLanSubIf(srcPortName, srcHost, phyLink.getPortOfLink(srcHost), vLanId);
        VLanSubIf dstIf = createVLanSubIf(dstPortName, dstHost, phyLink.getPortOfLink(dstHost), vLanId);

        vrfMgr.bindInterface(srcVrfId, srcPortName, srcIf);
        vrfMgr.bindInterface(dstVrfId, dstPortName, dstIf);

        VLanLink lk = new VLanLink();
        lk.setId(UUID.randomUUID().toString());
        lk.setSrcSubIf(srcIf.getId());
        lk.setDstSubIf(dstIf.getId());
        vLanDao.addVLanLink(lk);
        return lk.getId();
    }

    private VLanSubIf createVLanSubIf(String desc, String host, String port, int vLanId) {
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        InterfaceMgr ifMgr = getInstance(InterfaceMgr.class);

        VLanSubIf inf = new VLanSubIf();
        inf.setId(UUID.randomUUID().toString());
        inf.setHost(host);
        inf.setPort(port);
        inf.setVlanId(vLanId);

        ifMgr.addInterface(inf);

        PhyRouter pr = deviceMgr.getRouter(host);
        if (pr != null) {
            ConfigHolder ch = pr.addHolder(CLI.INTERFACE, inf.getInterfaceName());
            ch.addCommand("description", desc);
            ch.addCommand("encapsulation", "dot1q", String.valueOf(vLanId));
        }

        return inf;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        VLanDao vLanDao = getInstance(VLanDao.class);
        VrfMgr vrfMgr = getInstance(VrfMgr.class);
        InterfaceMgr ifMgr = getInstance(InterfaceMgr.class);

        VLanLink lk = vLanDao.getVLanLink(idInProvider);
        if (lk == null) {
            return;
        }

        VLanSubIf srcIf = (VLanSubIf)ifMgr.getInterface(lk.getSrcSubIf());
        VLanSubIf dstIf = (VLanSubIf)ifMgr.getInterface(lk.getDstSubIf());

        vrfMgr.unBindInterface(srcIf);
        vrfMgr.unBindInterface(dstIf);

        vLanDao.delVLanLink(idInProvider);
        deleteVLanSubIf(srcIf);
        deleteVLanSubIf(dstIf);
    }

    private void deleteVLanSubIf(VLanSubIf inf) {
        PhyDeviceMgr deviceMgr = getInstance(PhyDeviceMgr.class);
        InterfaceMgr ifMgr = getInstance(InterfaceMgr.class);

        ifMgr.delInterface(inf.getId());
        PhyRouter pr = deviceMgr.getRouter(inf.getHost());
        if (pr != null) {
            pr.undo(CLI.INTERFACE, inf.getInterfaceName());
        }
    }
}
