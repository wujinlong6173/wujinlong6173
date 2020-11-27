package wjl.demo;

import com.huawei.physical.*;
import wjl.docker.VirtualContainer;
import wjl.provider.ProviderMgr;

/**
 * 模拟多个运营商互联互通，可以理解为一个新的运营商，它自己没有路由器，
 * 单有很多纤缆，用于连接其它运营商的路由器。
 */
public class CrossIspMgrDemo extends AbstractIspMgr {
    public CrossIspMgrDemo(String ispName) {
        super(ispName);
    }

    @Override
    protected ProviderMgr createProviders() {
        VirtualContainer container = getContainer();
        ProviderMgr providerMgr = new ProviderMgr();

        PhyLinkMgr linkMgr = new PhyLinkMgr();
        container.setInstance(linkMgr);

        CrossLinkProvider linkProvider = new CrossLinkProvider();
        linkProvider.setContainer(container);
        providerMgr.addLinkProvider("互联链路", linkProvider);

        return providerMgr;
    }
}
