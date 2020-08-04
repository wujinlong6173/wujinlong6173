package wjl.net;

import org.junit.Test;
import wjl.net.provider.DeviceProvider;
import wjl.net.provider.LinkProvider;
import wjl.net.provider.ProviderLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void net1() {
        App app = new App();

        DeviceProvider vrfProvider = ProviderLoader.getDeviceProvider("com.huawei.vrf.VrfDeviceProvider");
        LinkProvider vlanProvider = ProviderLoader.getLinkProvider("com.huawei.vlan.VLanLinkProvider");
        Map<String,Object> inputs = new HashMap<>();
        
        inputs.put("name", "R1");
        inputs.put("host", "AG01");
        String r1 = app.createDevice("VRF01", vrfProvider, inputs);
        String p1 = app.createPort(r1, "eth0", null);
        
        inputs.put("name", "R2");
        inputs.put("host", "AG02");
        String r2 = app.createDevice("VRF02", vrfProvider, inputs);
        String p2 = app.createPort(r2, "eth1", null);
        
        inputs.clear();
        app.createLink(null, p1, p2, vlanProvider, inputs);
        app.getNet();
    }
}
