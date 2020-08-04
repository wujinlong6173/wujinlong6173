package wjl.net.intent;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备的意图。
 */
public class Device {
    private String id;
    private String name;
    
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
}
