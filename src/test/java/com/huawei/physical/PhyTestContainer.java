package com.huawei.physical;

import org.junit.Before;
import org.junit.Ignore;
import wjl.docker.VirtualContainer;

@Ignore
public class PhyTestContainer {
    protected VirtualContainer container;
    protected PhyDeviceMgr deviceMgr;
    protected PhyLinkMgr linkMgr;
    protected PhyLinkProvider linkProvider;
    protected PhyRouterProvider routerProvider;
    protected PhySwitchProvider switchProvider;

    @Before
    public void init() {
        container = new VirtualContainer();

        linkMgr = new PhyLinkMgr();
        container.setInstance(linkMgr);
        linkProvider = new PhyLinkProvider();
        linkProvider.setContainer(container);

        deviceMgr = new PhyDeviceMgr();
        container.setInstance(deviceMgr);
        routerProvider = new PhyRouterProvider();
        routerProvider.setContainer(container);
        switchProvider = new PhySwitchProvider();
        switchProvider.setContainer(container);
    }
}
