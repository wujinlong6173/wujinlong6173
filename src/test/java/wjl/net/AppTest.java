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

        DeviceProvider vrfProvider = ProviderLoader.getDeviceProvider("com.huawei.provider.VrfDeviceProvider");
        LinkProvider vlanProvider = ProviderLoader.getLinkProvider("com.huawei.provider.VLanLinkProvider");
        Map<String,Object> inputs = new HashMap<>();
        inputs.put("name", "R1");
        inputs.put("host", "AG01");
        app.createDevice("VRF01", vrfProvider, inputs);
        inputs.put("name", "R2");
        inputs.put("host", "AG02");
        app.createDevice("VRF02", vrfProvider, inputs);
        app.createLink("VRF01", "VRF02", vlanProvider, inputs);
        app.getNet();
    }
}