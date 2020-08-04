package wjl.net.intent;

/**
 * 端口的意图，意图和实现的映射关系保存在Provider中，L3NMS不保存。
 * 
 */
public class Port {
    /**
     * 设备的标识，端口必须属于某个设备。
     */
    private String devId;
    
    /**
     * 端口的全局唯一标识，管理端口的生命周期时，使用此标识。
     */
    private String id;

    /**
     * 端口在设备内的唯一名称，给端口下发配置命令时，使用名称作为标识。
     */
    private String name;
    
    private String description; 
    
    /**
     * 创建链路后填此参数。
     */
    private String linkId;
    
    /**
     * 端口在链路中的角色，取值范围待定。
     */
    private String role; 

    public String getDevId() {
        return devId;
    }
    
    public void setDevId(String devId) {
        this.devId = devId;
    }
    
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLinkId() {
        return linkId;
    }
    
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
