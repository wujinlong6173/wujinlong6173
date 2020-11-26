package wjl.demo;

import com.huawei.physical.*;
import com.huawei.vrf.VrfMgr;
import wjl.client.topo.TopoControlCenter;
import wjl.docker.VirtualContainer;
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
    private final VirtualContainer container;
    private final String ispName;
    private final Properties cfg;
    private final NetworkApi network;
    private final TopoControlCenter ccc;

    /**
     *
     * @param ispName 运营商的名称
     * @param namePrefix 自动给设备命名的前缀
     */
    public SingleIspMgrDemo(String ispName, String namePrefix) {
        super("L3NMS " + ispName);
        this.ispName = ispName;
        this.cfg = loadConfig(String.format(Locale.ENGLISH, "/demo/%s.properties", ispName));

        this.container = new VirtualContainer();
        PhyLinkMgr linkMgr = new PhyLinkMgr();
        PhyDeviceMgr deviceMgr = new PhyDeviceMgr();
        VrfMgr vrfMgr = new VrfMgr();
        this.container.setInstance(linkMgr);
        this.container.setInstance(deviceMgr);
        this.container.setInstance(vrfMgr);

        ProviderMgr providerMgr = createProviders();
        this.network = new NetworkApi(providerMgr);
        this.ccc = new TopoControlCenter(network, providerMgr);
        this.ccc.setDeviceNamePrefix(namePrefix);

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

    private ProviderMgr createProviders() {
        ProviderMgr providerMgr = new ProviderMgr();

        PhyRouterProvider routerProvider = new PhyRouterProvider();
        routerProvider.setContainer(container);
        providerMgr.addDeviceProvider("物理路由器", routerProvider);

        PhySwitchProvider switchProvider = new PhySwitchProvider();
        switchProvider.setContainer(container);
        providerMgr.addDeviceProvider("物理交换机", switchProvider);

        PhyLinkProvider linkProvider = new PhyLinkProvider();
        linkProvider.setContainer(container);
        providerMgr.addLinkProvider("物理链路", linkProvider);

        return providerMgr;
    }
}
