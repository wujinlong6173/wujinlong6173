package com.huawei.vlan;

import com.huawei.inventory.LinkMgr;
import com.huawei.schema.SchemaParser;
import com.huawei.vrf.VrfMgr;

import wjl.net.provider.LinkProvider;
import wjl.net.provider.ProviderException;
import wjl.net.schema.ObjectSchema;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class VLanLinkProvider implements LinkProvider {
    private final ObjectSchema createSchema;

    public VLanLinkProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("HW.VLan", "properties:\n" +
                "link: {type: string, flag: CR}\n" +
                "vlanId: {type: integer, flag: CR}\n");
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
            String srcOuterId, String srcPortName, String srcProvider,  
            String dstOuterId, String dstPortName, String dstProvider, 
            Map<String, Object> inputs) throws ProviderException {
        
        String srcHost = VrfMgr.getHostOfVrf(srcOuterId);
        String dstHost = VrfMgr.getHostOfVrf(dstOuterId);
        if (srcHost == null || dstHost == null) {
            throw new ProviderException(ProviderException.OBJECT_NOT_EXIST, 
                    String.format(Locale.ENGLISH, "virtual router %s or %s don't exist.",
                            srcOuterId, dstOuterId));
        }
        
        String phyLink = LinkMgr.findLinkBetweenDevice(srcHost, dstHost);
        if (phyLink == null) {
            throw new ProviderException(ProviderException.NO_USABLE_RESOURCE,
                    String.format(Locale.ENGLISH, "no usable link between %s and %s.",
                            srcHost, dstHost));
        }
        
        int vlanId = LinkMgr.allocVlanId(phyLink);

        VlanSubIf srcIf = new VlanSubIf();
        srcIf.setId(UUID.randomUUID().toString());
        srcIf.setPort(LinkMgr.getPortOfLink(phyLink, srcHost));
        srcIf.setVlanId(vlanId);
        
        VlanSubIf dstIf = new VlanSubIf();
        dstIf.setId(UUID.randomUUID().toString());
        dstIf.setPort(LinkMgr.getPortOfLink(phyLink, dstHost));
        dstIf.setVlanId(vlanId);
        
        VlanLink lk = new VlanLink();
        lk.setId(UUID.randomUUID().toString());
        return lk.getId();
    }
}
