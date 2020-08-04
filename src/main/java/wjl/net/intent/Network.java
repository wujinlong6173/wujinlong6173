package wjl.net.intent;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个Network对象表示一个网络。
 */
public class Network {
    /**
     * 网络中的所有设备。
     */
    private Map<String, Device> devices = new HashMap<>();
    
    /**
     * 网络中的所有端口。
     */
    private Map<String, Port> ports = new HashMap<>();
    
    /**
     * 网络中的所有链路。
     */
    private Map<String, Link> links = new HashMap<>();
    
    public void addPort(Port pt) {
        ports.put(pt.getId(), pt);
    }
}
