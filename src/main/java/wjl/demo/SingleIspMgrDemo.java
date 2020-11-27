package wjl.demo;

import com.huawei.physical.*;
import com.huawei.vrf.VrfMgr;
import wjl.docker.VirtualContainer;
import wjl.provider.ProviderMgr;

/**
 * 模拟一个运营商，每个对象实例代表一个运营商，管理一张网络。
 * 实际应用时，每个运营商都部署一套L3NMS系统。
 */
public class SingleIspMgrDemo extends AbstractIspMgr {
    /**
     *
     * @param ispName 运营商的名称
     */
    public SingleIspMgrDemo(String ispName) {
        super(ispName);
    }

    @Override
    protected ProviderMgr createProviders() {
        VirtualContainer container = getContainer();
        ProviderMgr providerMgr = new ProviderMgr();

        PhyLinkMgr linkMgr = new PhyLinkMgr();
        PhyDeviceMgr deviceMgr = new PhyDeviceMgr();
        VrfMgr vrfMgr = new VrfMgr();
        container.setInstance(linkMgr);
        container.setInstance(deviceMgr);
        container.setInstance(vrfMgr);

        PhyRouterProvider routerProvider = new PhyRouterProvider();
        routerProvider.setContainer(container);
        providerMgr.addDeviceProvider("物理路由器", routerProvider);

        PhySwitchProvider switchProvider = new PhySwitchProvider();
        switchProvider.setContainer(container);
        providerMgr.addDeviceProvider("物理交换机", switchProvider);

        PhyLinkProvider linkProvider = new PhyLinkProvider();
        linkProvider.setContainer(container);
        providerMgr.addLinkProvider("物理链路", linkProvider);

        return providerMgr;
    }
}
