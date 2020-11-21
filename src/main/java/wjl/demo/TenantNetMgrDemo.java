package wjl.demo;

import wjl.client.topo.TopoControlCenter;
import wjl.net.NetworkApi;
import wjl.provider.ProviderMgr;

import javax.swing.*;
import java.awt.*;

/**
 * 模拟一个租户，每个对象实例代表一个租户，管理一张网络。
 */
public class TenantNetMgrDemo extends JFrame {
    private final String tenantName;
    private final NetworkApi network;
    private final TopoControlCenter ccc;

    /**
     *
     * @param tenantName 租户名称，唯一标识
     * @param providerMgr 本网络选中的供应商集合
     */
    public TenantNetMgrDemo(String tenantName, ProviderMgr providerMgr) {
        super("L3NMS " + tenantName);
        this.tenantName = tenantName;
        this.network = new NetworkApi(providerMgr);
        this.ccc = new TopoControlCenter(network, providerMgr);

        // 初始化窗口
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(ccc.getComponent(), BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(600, 400);
        setLocation(200, 100);
    }

    public String getTenantName() {
        return tenantName;
    }
}
