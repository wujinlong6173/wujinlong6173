package com.huawei.provider;

import com.huawei.schema.SchemaParser;
import wjl.net.provider.LinkProvider;
import wjl.net.schema.ObjectSchema;

public class VLanLinkProvider implements LinkProvider {
    private final ObjectSchema createSchema;

    public VLanLinkProvider() {
        SchemaParser parser = new SchemaParser();
        createSchema = parser.parse("HW.VLan", "properties:\n" +
                "link: {type: string, flag: CR}\n" +
                "vlanId: {type: integer, flag: CR}\n");
    }
    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }
}
