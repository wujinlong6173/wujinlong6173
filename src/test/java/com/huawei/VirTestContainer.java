package com.huawei;

import com.huawei.physical.PhyTestContainer;
import com.huawei.vrf.VrfDeviceProvider;
import com.huawei.vrf.VrfMgr;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class VirTestContainer extends PhyTestContainer {
    protected VrfMgr vrfMgr;
    protected VrfDeviceProvider vrfProvider;

    @Before
    public void init() {
        super.init();

        vrfMgr = new VrfMgr();
        container.setInstance(vrfMgr);

        vrfProvider = new VrfDeviceProvider();
        vrfProvider.setContainer(container);
    }
}
