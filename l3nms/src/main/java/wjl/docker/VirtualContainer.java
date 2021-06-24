package wjl.docker;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟运行容器，隔离不同的运营商和租户。
 */
public class VirtualContainer {
    private final Map<Class<? extends VirtualMember>, Object> singleInstances = new HashMap<>();

    public void setInstance(VirtualMember instance) {
        singleInstances.put(instance.getClass(), instance);
        instance.setContainer(this);
    }

    public <T> T getInstance(Class<? extends VirtualMember> cls) {
        return (T)singleInstances.get(cls);
    }
}
