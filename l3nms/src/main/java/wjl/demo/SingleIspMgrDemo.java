package wjl.demo;

import com.huawei.common.InterfaceMgr;
import com.huawei.physical.*;
import com.huawei.vlan.VLanDao;
import com.huawei.vrf.VpnRes;
import com.huawei.vrf.VrfMgr;
import wjl.docker.VirtualContainer;
import wjl.provider.ProductProviderMgr;

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
    protected ProductProviderMgr createProviders() {
        VirtualContainer container = getContainer();
        ProductProviderMgr productMgr = new ProductProviderMgr();

        PhyLinkMgr linkMgr = new PhyLinkMgr();
        PhyDeviceMgr deviceMgr = new PhyDeviceMgr();
        VrfMgr vrfMgr = new VrfMgr();
        VLanDao vLanDao = new VLanDao();
        InterfaceMgr ifMgr = new InterfaceMgr();
        VpnRes vpnRes = new VpnRes();
        container.setInstance(linkMgr);
        container.setInstance(deviceMgr);
        container.setInstance(vrfMgr);
        container.setInstance(vLanDao);
        container.setInstance(ifMgr);
        container.setInstance(vpnRes);

        PhyRouterProvider routerProvider = new PhyRouterProvider("", "物理路由器");
        routerProvider.setContainer(container);
        productMgr.addDeviceProvider(routerProvider);

        PhySwitchProvider switchProvider = new PhySwitchProvider("", "物理交换机");
        switchProvider.setContainer(container);
        productMgr.addDeviceProvider(switchProvider);

        PhyLinkProvider linkProvider = new PhyLinkProvider("", "物理链路");
        linkProvider.setContainer(container);
        productMgr.addLinkProvider(linkProvider);

        return productMgr;
    }
}
