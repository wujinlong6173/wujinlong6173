package wjl.provider;

import wjl.datamodel.schema.ObjectSchema;

import java.util.Map;
import java.util.UUID;

/**
 * 虚无的链路供应商
 */
public class DummyLinkProvider implements LinkProvider {

    @Override
    public String getName() {
        return DummyLinkProvider.class.getName();
    }

    @Override
    public ObjectSchema getCreateSchema() {
        return null;
    }

    @Override
    public String create(String idInNms,
                         String srcOuterId, String srcPortName, String srcProvider,
                         String dstOuterId, String dstPortName, String dstProvider,
                         Map<String, Object> inputs) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void delete(String idInProvider, Map<String, Object> inputs) {

    }
}
