package wjl.mapping.core.model;

/**
 * 数据搬运工，从数据提供者复制数据到接收者。
 * 例如，将模板输入的 abc.name 复制到模板输出的 items[0]。
 */
public class DataPorter {
    private final DataProvider provider;
    private final DataRecipient recipient;
    private final SimplePath srcPath;
    private final SimplePath dstPath;

    /**
     * 构造数据搬运工
     *
     * @param provider 数据提供者
     * @param recipient 数据接收者
     * @param srcPath 源路径
     * @param dstPath 目标路径
     */
    DataPorter(DataProvider provider, DataRecipient recipient, SimplePath srcPath, SimplePath dstPath) {
        this.provider = provider;
        this.recipient = recipient;
        this.srcPath = srcPath;
        this.dstPath = dstPath;

        provider.addOut(this);
        recipient.addIn(this);
    }

    public DataProvider getProvider() {
        return provider;
    }

    public DataRecipient getRecipient() {
        return recipient;
    }

    public SimplePath getSrcPath() {
        return srcPath;
    }

    public SimplePath getDstPath() {
        return dstPath;
    }
}
