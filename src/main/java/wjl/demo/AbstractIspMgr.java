package wjl.demo;

import wjl.client.topo.TopoControlCenter;
import wjl.docker.VirtualContainer;
import wjl.net.NetworkApi;
import wjl.provider.DeviceProvider;
import wjl.provider.ProductProviderMgr;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public abstract class AbstractIspMgr extends JFrame {
    private final VirtualContainer container;
    private final String ispName;
    private final Properties cfg;

    public AbstractIspMgr(String ispName) {
        super("L3NMS " + ispName);
        this.ispName = ispName;
        this.container = new VirtualContainer();
        this.cfg = loadConfig(String.format(Locale.ENGLISH, "/demo/%s.properties", ispName));

    }

    public String getIspName() {
        return ispName;
    }

    public VirtualContainer getContainer() {
        return container;
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

    public AbstractIspMgr init(DeviceProvider... deviceProviders) {
        ProductProviderMgr productMgr = createProviders();
        for (DeviceProvider provider : deviceProviders) {
            productMgr.addDeviceProvider(provider);
        }

        NetworkApi network = new NetworkApi(productMgr);
        TopoControlCenter ccc = new TopoControlCenter(network, productMgr);
        ccc.setDeviceNamePrefix(this.cfg.getProperty("name_prefix"));

        // 初始化窗口
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(ccc.getComponent(), BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(600, 400);
        setLocation(200, 100);
        return this;
    }

    protected abstract ProductProviderMgr createProviders();
}
