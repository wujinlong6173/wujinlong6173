package wjl.util;

/**
 * 程序的配置参数
 */
public class Config {
    private static Config instance;
    
    public static void load(String filename) {
        instance = YamlLoader.fileToObject(Config.class, filename);
        if (instance == null) {
            instance = new Config();
        }
    }
    
    public static Config instance() {
        return instance;
    }
    
    /**
     * Telnet客户端程序的路径
     */
    private String telnetClient;

    public String getTelnetClient() {
        return telnetClient;
    }

    public void setTelnetClient(String telnetClient) {
        this.telnetClient = telnetClient;
    }
}
