package wjl.demo;

import wjl.client.topo.TopoControlCenter;
import wjl.net.NetworkApi;
import wjl.provider.ProviderMgr;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * 模拟一个运营商，每个对象实例代表一个运营商，管理一张网络。
 * 实际应用时，每个运营商都部署一套L3NMS系统。
 */
public class SingleIspMgrDemo extends JFrame {
    private final String ispName;
    private final Properties cfg;
    private final NetworkApi network;
    private final TopoControlCenter ccc;

    /**
     *
     * @param ispName 运营商的名称
     * @param providerMgr 本网络选中的供应商集合
     */
    public SingleIspMgrDemo(String ispName, ProviderMgr providerMgr) {
        super("L3NMS " + ispName);
        this.ispName = ispName;
        this.cfg = loadConfig(String.format(Locale.ENGLISH, "/demo/%s.properties", ispName));
        this.network = new NetworkApi(providerMgr);
        this.ccc = new TopoControlCenter(network, providerMgr);
        this.ccc.setDeviceNamePrefix(ispName);

        // 初始化窗口
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(ccc.getComponent(), BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(600, 400);
        setLocation(200, 100);
    }

    public String getIspName() {
        return ispName;
    }

    private Properties loadConfig(String fileName) {
        Properties cfg = new Properties();
        try (InputStream isCfg = SingleIspMgrDemo.class.getResourceAsStream(fileName)) {
            cfg.load(isCfg);
        } catch (IOException err) {
            err.printStackTrace();
        }
        return cfg;
    }
}
