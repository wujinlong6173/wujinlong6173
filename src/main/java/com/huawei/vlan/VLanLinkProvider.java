package com.huawei.vlan;

import com.huawei.schema.SchemaParser;
import wjl.net.provider.LinkProvider;
import wjl.net.provider.ProviderException;
import wjl.net.schema.ObjectSchema;

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
        
        String phyLink = (String)inputs.get("phyLink");
        Integer vlanId = (Integer)inputs.get("vlanId");
        
        VlanSubIf srcIf = new VlanSubIf();
        srcIf.setId(UUID.randomUUID().toString());
        srcIf.setPhyLink(phyLink);
        srcIf.setVlanId(vlanId == null ? 0 : vlanId);
        
        VlanSubIf dstIf = new VlanSubIf();
        dstIf.setId(UUID.randomUUID().toString());
        dstIf.setPhyLink(phyLink);
        dstIf.setVlanId(vlanId == null ? 0 : vlanId);
        
        VlanLink lk = new VlanLink();
        lk.setId(UUID.randomUUID().toString());
        return lk.getId();
    }
}
