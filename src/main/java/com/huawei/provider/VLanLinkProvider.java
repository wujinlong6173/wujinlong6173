package com.huawei.provider;

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
        return null;
    }

    @Override
    public String create(String idInNms, String srcOuterId, String dstOuterId,
            String srcProvider, String dstProvider, Map<String, Object> inputs) throws ProviderException {
        return UUID.randomUUID().toString();
    }
}
