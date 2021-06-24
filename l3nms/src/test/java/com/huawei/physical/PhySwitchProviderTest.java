package com.huawei.physical;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PhySwitchProviderTest extends PhyTestContainer {
    PhySwitchProvider psp = new PhySwitchProvider("test", "PS");

    @Test
    public void createSwitch() {
        psp.setContainer(container);

        Map<String, Object> inputs = new HashMap<>();
        inputs.put("name", "S1");
        String s1 = psp.create("id-s1", inputs);
        inputs.put("name", "S2");
        String s2 = psp.create("id-s2", inputs);

        PhyDevice ps1 = deviceMgr.getDevice(s1);
        assertTrue(ps1 instanceof PhySwitch);

        psp.delete(s2, inputs);
        assertNull(deviceMgr.getDevice(s2));
    }
}