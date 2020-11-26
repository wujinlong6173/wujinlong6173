package wjl.docker;

public interface VirtualMember {
    void setContainer(VirtualContainer container);

    <T> T getInstance(Class<? extends VirtualMember> cls);
}
