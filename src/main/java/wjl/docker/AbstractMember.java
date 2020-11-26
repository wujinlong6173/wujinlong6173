package wjl.docker;

public abstract class AbstractMember implements VirtualMember {
    private VirtualContainer container;

    public void setContainer(AbstractMember other) {
        this.container = other.container;
    }

    @Override
    public void setContainer(VirtualContainer container) {
        this.container = container;
    }

    @Override
    public <T> T getInstance(Class<? extends VirtualMember> cls) {
        return container.getInstance(cls);
    }
}
