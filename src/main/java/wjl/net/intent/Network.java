package wjl.net.intent;

import wjl.net.NetworkSummary;

import java.util.Collection;
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

    public Collection<Device> getAllDevices() {
        return devices.values();
    }

    public Collection<Port> getAllPorts() {
        return ports.values();
    }

    public Collection<Link> getAllLinks() {
        return links.values();
    }
    
    public void addDevice(Device dev) {
        devices.put(dev.getId(), dev);
    }
    
    public Device getDevice(String id) {
        return devices.get(id);
    }
    
    public void delDevice(String id) {
        devices.remove(id);
    }
    
    public void addPort(Port pt) {
        ports.put(pt.getId(), pt);
    }
    
    public Port getPort(String id) {
        return ports.get(id);
    }
    
    public void delPort(String id) {
        ports.remove(id);
    }
    
    public void addLink(Link lk) {
        links.put(lk.getId(), lk);
    }
    
    public Link getLink(String id) {
        return links.get(id);
    }

    public void delLink(String id) {
        links.remove(id);
    }

    public void summary(NetworkSummary sum) {
        sum.setDevice(devices.size());
        sum.setPort(ports.size());
        sum.setLink(links.size());
    }
}
