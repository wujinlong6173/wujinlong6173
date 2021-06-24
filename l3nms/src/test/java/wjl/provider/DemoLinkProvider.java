package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

import java.util.Map;

public class DemoLinkProvider extends AbsProductProvider implements LinkProvider {
    public DemoLinkProvider(String providerName, String productName) {
        super(providerName, productName);
    }

    @Override
    public String create(String idInNms,
                         String srcOuterId, String srcPortName, String srcProvider,
                         String dstOuterId, String dstPortName, String dstProvider,
                         Map<String, Object> inputs) {
        return idInNms;
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {

    }

    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }
}
