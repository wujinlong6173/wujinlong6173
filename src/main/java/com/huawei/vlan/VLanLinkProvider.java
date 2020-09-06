package com.huawei.vlan;

import com.huawei.inventory.HuaWeiInventory;
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
        
        String phyLink = HuaWeiInventory.findLinkBetweenDevice(srcHost, dstHost);
        if (phyLink == null) {
            throw new ProviderException(ErrorType.NO_USABLE_RESOURCE,
                    String.format(Locale.ENGLISH, "no usable link between %s and %s.",
                            srcHost, dstHost));
        }
        
        int vLanId = HuaWeiInventory.allocVlanId(phyLink);

        VLanSubIf srcIf = new VLanSubIf();
        srcIf.setId(UUID.randomUUID().toString());
        srcIf.setHost(srcHost);
        srcIf.setPort(HuaWeiInventory.getPortOfLink(phyLink, srcHost));
        srcIf.setVlanId(vLanId);

        VLanSubIf dstIf = new VLanSubIf();
        dstIf.setId(UUID.randomUUID().toString());
        dstIf.setHost(dstHost);
        dstIf.setPort(HuaWeiInventory.getPortOfLink(phyLink, dstHost));
        dstIf.setVlanId(vLanId);

        VrfMgr.bindInterface(srcVrfId, srcPortName, srcIf);
        VrfMgr.bindInterface(dstVrfId, dstPortName, dstIf);
        VLanLink lk = new VLanLink();
        lk.setId(UUID.randomUUID().toString());
        lk.setSrcSubIf(srcIf.getId());
        lk.setDstSubIf(dstIf.getId());

        VLanDao.addVLanLink(lk);
        VLanDao.addVLanSubIf(srcIf);
        VLanDao.addVLanSubIf(dstIf);
        return lk.getId();
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) throws ProviderException {
        VLanLink lk = VLanDao.getVLanLink(idInProvider);
        if (lk == null) {
            return;
        }

        VLanSubIf srcIf = VLanDao.getVLanSubIf(lk.getSrcSubIf());
        VLanSubIf dstIf = VLanDao.getVLanSubIf(lk.getDstSubIf());
        VrfMgr.unBindInterface(srcIf);
        VrfMgr.unBindInterface(dstIf);

        VLanDao.delVLanLink(idInProvider);
        VLanDao.delVLanSubIf(lk.getSrcSubIf());
        VLanDao.delVLanSubIf(lk.getDstSubIf());
    }
}
