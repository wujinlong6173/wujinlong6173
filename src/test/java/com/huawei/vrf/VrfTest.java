package com.huawei.vrf;

import com.huawei.vlan.VLanSubIf;
import org.junit.Test;

import static org.junit.Assert.*;

public class VrfTest {
    @Test
    public void bindInterface() {
        Vrf vrf = new Vrf();
        vrf.setId("100");
        VLanSubIf s1 = new VLanSubIf();
        VLanSubIf s2 = new VLanSubIf();
        VLanSubIf s3 = new VLanSubIf();
        vrf.bindInterface("Eth1", s1);
        vrf.bindInterface("Eth2", s2);
        vrf.bindInterface("Eth3", s3);
        assertEquals(3, vrf.getBindInterfaces().size());
        assertEquals(vrf.getId(), s1.getBindService());

        vrf.unBindInterface(s1);
        assertNull(s1.getBindService());
        assertEquals(2, vrf.getBindInterfaces().size());
    }
}