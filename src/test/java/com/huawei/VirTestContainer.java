package com.huawei;

import com.huawei.common.InterfaceMgr;
import com.huawei.physical.PhyTestContainer;
import com.huawei.vlan.VLanDao;
import com.huawei.vrf.VpnRes;
import com.huawei.vrf.VrfDeviceProvider;
import com.huawei.vrf.VrfMgr;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class VirTestContainer extends PhyTestContainer {
    protected VrfMgr vrfMgr;
    protected VLanDao vLanDao;
    protected InterfaceMgr ifMgr;
    protected VpnRes vpnRes;
    protected VrfDeviceProvider vrfProvider;

    @Before
    public void init() {
        super.init();

        vrfMgr = new VrfMgr();
        vLanDao = new VLanDao();
        ifMgr = new InterfaceMgr();
        vpnRes = new VpnRes();
        container.setInstance(vrfMgr);
        container.setInstance(vLanDao);
        container.setInstance(ifMgr);
        container.setInstance(vpnRes);

        vrfProvider = new VrfDeviceProvider(null, null);
        vrfProvider.setContainer(container);
    }
}
