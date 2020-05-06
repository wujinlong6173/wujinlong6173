package wjl.net;

import wjl.net.provider.DeviceProvider;
import wjl.net.provider.LinkProvider;

import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    void createDevice(String devId, DeviceProvider provider, Map<String,Object> inputs) {
        provider.create(inputs);
    }

    void createLink(String srcDevId, String dstDevId, LinkProvider provider, Map<String,Object> inputs) {

    }

    void getNet() {

    }
}
