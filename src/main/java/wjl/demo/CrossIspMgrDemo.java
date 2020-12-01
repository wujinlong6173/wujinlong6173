package wjl.demo;

import com.huawei.physical.*;
import com.huawei.vlan.VLanDao;
import wjl.docker.VirtualContainer;
import wjl.provider.ProductProviderMgr;

/**
 * 模拟多个运营商互联互通，可以理解为一个新的运营商，它自己没有路由器，
 * 单有很多纤缆，用于连接其它运营商的路由器。
 * 借鉴银联和银行的关系，创建跨运营商的VLan链路，就像跨行转账。
 */
public class CrossIspMgrDemo extends AbstractIspMgr {
    public CrossIspMgrDemo(String ispName) {
        super(ispName);
    }

    @Override
    protected ProductProviderMgr createProviders() {
        VirtualContainer container = getContainer();
        ProductProviderMgr productMgr = new ProductProviderMgr();

        PhyLinkMgr linkMgr = new PhyLinkMgr();
        VLanDao vLanDao = new VLanDao();
        container.setInstance(linkMgr);
        container.setInstance(vLanDao);

        CrossLinkProvider linkProvider = new CrossLinkProvider("互联", "物理链路");
        linkProvider.setContainer(container);
        productMgr.addLinkProvider(linkProvider);

        return productMgr;
    }
}
