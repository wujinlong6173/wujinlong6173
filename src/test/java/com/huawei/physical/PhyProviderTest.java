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
        PhyLinkProvider linkProvider = new PhyLinkProvider();
        Map<String,Object> inputs = new HashMap<>();

        inputs.put("name", "YD-1");
        String yd1 = routerProvider.create("nms-1", inputs);
        inputs.put("name", "YD-2");
        String yd2 = routerProvider.create("nms-2", inputs);
        inputs.put("name", "YD-3");
        String yd3 = routerProvider.create("nms-3", inputs);
        String l1 = linkProvider.create("nms-4", yd1, "GE0", null, yd2, "GE1", null, inputs);
        String l2 = linkProvider.create("nms-5", yd2, "GE0", null, yd3, "GE1", null, inputs);

        assertNotNull(PhyRouterMgr.getRouter(yd1));
        assertNotNull(PhyRouterMgr.getRouter(yd2));
        assertNotNull(PhyRouterMgr.getRouter(yd3));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice(yd1, yd2));
        assertNotNull(PhyLinkMgr.findLinkBetweenDevice(yd2, yd3));
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
        assertNull(PhyRouterMgr.getRouter(yd1));
        assertNull(PhyRouterMgr.getRouter(yd2));
        assertNull(PhyRouterMgr.getRouter(yd3));
    }
}
