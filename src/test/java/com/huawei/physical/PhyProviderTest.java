package com.huawei.physical;

import org.junit.Test;
import wjl.provider.ProviderException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * 测试物理设备和物理链路的供应商
 */
public class PhyProviderTest {
    @Test
    public void createPhysicalNetwork() throws ProviderException {
        PhyRouterProvider routerProvider = new PhyRouterProvider();
        PhySwitchProvider switchProvider = new PhySwitchProvider();
        PhyLinkProvider linkProvider = new PhyLinkProvider();
        Map<String,Object> inputs = new HashMap<>();

        // 添加物理路由器
        inputs.put("name", "YD-1");
        String yd1 = routerProvider.create("nms-1", inputs);
        inputs.put("name", "YD-2");
        String yd2 = routerProvider.create("nms-2", inputs);
        inputs.put("name", "YD-3");
        String yd3 = routerProvider.create("nms-3", inputs);

        // 添加物理交换机
        inputs.put("name", "SW-1");
        String sw1 = switchProvider.create("nms-sw-1", inputs);
        inputs.put("name", "SW-2");
        String sw2 = switchProvider.create("nms-sw-2", inputs);

        // 添加物理链路
        String l1 = linkProvider.create("nms-4", yd1, "GE0", null, yd2, "GE1", null, inputs);
        String l2 = linkProvider.create("nms-5", yd2, "GE0", null, yd3, "GE1", null, inputs);
        String l3 = linkProvider.create("nms-6", yd3, "GE2/0", null, sw1, "GE1", null, inputs);
        String l4 = linkProvider.create("nms-7", yd3, "GE2/1", null, sw2, "GE1", null, inputs);

        assertNotNull(PhyDeviceMgr.getRouter(yd1));
        assertNotNull(PhyDeviceMgr.getRouter(yd2));
        assertNotNull(PhyDeviceMgr.getRouter(yd3));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice(yd1, yd2));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice(yd2, yd3));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice(sw1, yd3));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice(sw2, yd3));
        assertNull(PhyLinkMgr.findLinkBetweenDevice(yd1, yd3));

        // 删除链路
        linkProvider.delete(l1, inputs);
        linkProvider.delete(l2, inputs);
        assertNull(PhyLinkMgr.findLinkBetweenDevice(yd1, yd2));
        assertNull(PhyLinkMgr.findLinkBetweenDevice(yd2, yd3));

        // 删除设备
        routerProvider.delete(yd1, inputs);
        routerProvider.delete(yd2, inputs);
        routerProvider.delete(yd3, inputs);
        assertNull(PhyDeviceMgr.getRouter(yd1));
        assertNull(PhyDeviceMgr.getRouter(yd2));
        assertNull(PhyDeviceMgr.getRouter(yd3));
    }
}
