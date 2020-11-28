package wjl.provider;

import wjl.docker.AbstractMember;

public abstract class AbsProductProvider extends AbstractMember
        implements ProductProvider {
    private final String providerName;
    private final String productName;

    /**
     *
     * @param providerName 供应商的名称
     * @param productName 产品的名称
     */
    public AbsProductProvider(String providerName, String productName) {
        this.providerName = providerName;
        this.productName = productName;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public String getProductName() {
        return productName;
    }
}
