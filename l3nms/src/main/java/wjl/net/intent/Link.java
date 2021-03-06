package wjl.net.intent;

import java.util.ArrayList;
import java.util.List;

/**
 * 链路的意图。
 */
public class Link {
    private String id;
    private List<Port> ports = new ArrayList<>();
    private boolean deploy;
    
    /**
     * 如果两个设备之间存在多条链路，建议用名称加以区分。
     */
    private String name;
    
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
        ports.add(pt);
        pt.setLinkId(id);
    }

    public List<Port> getPorts() {
        return ports;
    }
    
    public void removeAllPorts() {
        for (Port pt : ports) {
            pt.setLinkId(null);
        }
        ports.clear();
    }
    
    public boolean isDeploy() {
        return deploy;
    }

    public void setDeploy(boolean deploy) {
        this.deploy = deploy;
    }
}
