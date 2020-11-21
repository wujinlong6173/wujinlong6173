package wjl.net;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkApiTest {

    @Test
    public void testNetworkIntent() throws NetworkException {
        NetworkApi api = new NetworkApi(null);
        String r1 = api.createDevice("Router1");
        String r2 = api.createDevice("Router2");
        String p1 = api.createPort(r1, "Eth1", null);
        String p2 = api.createPort(r2, "Eth2", null);
        String lk1 = api.createLink("Link1", p1, p2);
        NetworkSummary sum1 = api.summary();
        assertEquals(2, sum1.getDevice());
        assertEquals(2, sum1.getPort());
        assertEquals(1, sum1.getLink());

        api.deleteLink(lk1);
        api.deleteDevice(r1);
        api.deleteDevice(r2);
        NetworkSummary sum2 = api.summary();
        assertEquals(0, sum2.getDevice());
        assertEquals(0, sum2.getPort());
        assertEquals(0, sum2.getLink());
    }
}