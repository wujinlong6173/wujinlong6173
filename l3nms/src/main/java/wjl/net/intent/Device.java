package wjl.net.intent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备的意图。
 */
public class Device {
    private String id;
    private String name;
    private boolean deploy;
    
    /**
     * 设备在拓扑图上的坐标，单位为DIP，设备无关像素。
     */
    private double dipX;
    private double dipY;
    
    /**
     * 设备内的所有端口，以端口名称为索引。
     */
    private Map<String, Port> ports = new HashMap<>();
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void addPort(Port pt) {
        ports.put(pt.getName(), pt);
    }
    
    public Port getPort(String portName) {
        return ports.get(portName);
    }
    
    public Collection<Port> getPorts() {
        return ports.values();
    }

    public void removePort(String portName) {
        ports.remove(portName);
    }
    
    public boolean hasLinks() {
        for (Port pt : ports.values()) {
            if (pt.getLinkId() != null) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isDeploy() {
        return deploy;
    }

    public void setDeploy(boolean deploy) {
        this.deploy = deploy;
    }

    public double getDipX() {
        return dipX;
    }

    public void setDipX(double dipX) {
        this.dipX = dipX;
    }

    public double getDipY() {
        return dipY;
    }

    public void setDipY(double dipY) {
        this.dipY = dipY;
    }
}
