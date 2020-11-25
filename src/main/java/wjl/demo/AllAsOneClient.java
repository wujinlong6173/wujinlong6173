package wjl.demo;

import com.huawei.cli.HuaWeiSystem;
import com.huawei.physical.PhyLinkProvider;
import com.huawei.physical.PhyRouterProvider;
import com.huawei.physical.PhySwitchProvider;
import com.huawei.vlan.VLanLinkProvider;
import com.huawei.vrf.VrfDeviceProvider;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import wjl.client.topo.TopoControlCenter;
import wjl.provider.ProviderMgr;
import wjl.ssh.MySshServer;
import wjl.util.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有东西放在一个进程中运行，提供一个界面。
 * JVM参数：-Dfile.encoding=gbk
 */
public class AllAsOneClient implements ActionListener {
    private static final String CMD_SWITCH_TENANT = "cmd_switch_tenant";
    private final ProviderMgr forTenant = new ProviderMgr();
    private final Map<String, TopoControlCenter> allTenants = new HashMap<>();
    private JFrame mainFrame; // 主窗口，方便修改标题
    private JSplitPane mainPane; // 主界面，方便切换租户拓扑

    public static void main(String[] args) {
        // 设置程序窗口的外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
        mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";

        Config.load();

        HuaWeiSystem hws = new HuaWeiSystem();
        MySshServer.start(22, hws, hws);
        new AllAsOneClient().start();
    }

    void start() {
        forTenant.addDeviceProvider("VRF", new VrfDeviceProvider());
        forTenant.addLinkProvider("VLan", new VLanLinkProvider());

        JMenuBar menuBar = createMainMenu();
        JPanel mainPanel = createMainPanel();
        mainFrame = createMainFrame(menuBar, mainPanel);
        mainFrame.setTitle("L3NMS");
        mainFrame.setVisible(true);
        switchTenant("TenantA");
    }

    private void switchTenant(String tenantName) {
        TopoControlCenter tcc = allTenants.get(tenantName);
        if (tcc == null) {
            tcc = new TopoControlCenter(null, forTenant);
            tcc.setDeviceNamePrefix(tenantName);
            allTenants.put(tenantName, tcc);
        }

        mainFrame.setTitle("L3NMS " + tenantName);
        mainPane.setRightComponent(tcc.getComponent());
    }

    JMenuBar createMainMenu() {
        ProviderMgr forMobile = new ProviderMgr();
        forMobile.addDeviceProvider("物理路由器", new PhyRouterProvider());
        forMobile.addDeviceProvider("物理交换机", new PhySwitchProvider());
        forMobile.addLinkProvider("物理链路", new PhyLinkProvider());
        SingleIspMgrDemo ia = new SingleIspMgrDemo("移动", "YD", forMobile);

        ProviderMgr forTel = new ProviderMgr();
        forTel.addDeviceProvider("物理路由器", new PhyRouterProvider());
        forTel.addDeviceProvider("物理交换机", new PhySwitchProvider());
        forTel.addLinkProvider("物理链路", new PhyLinkProvider());
        SingleIspMgrDemo ib = new SingleIspMgrDemo("电信", "DX", forTel);

        JMenu isp = new JMenu("运营商");
        isp.add(new ShowFrameAction(ia.getIspName(), ia));
        isp.add(new ShowFrameAction(ib.getIspName(), ib));
        isp.add(new CrossIspMgrDemo("互联互通"));

        JMenu tenant = new JMenu("租户");
        tenant.add(tenantMenuItem("TenantA"));
        tenant.add(tenantMenuItem("TenantB"));
        tenant.add(tenantMenuItem("TenantC"));
        tenant.add(tenantMenuItem("TenantD"));

        JMenuBar bar = new JMenuBar();
        bar.add(isp);
        bar.add(tenant);
        return bar;
    }

    private JMenuItem tenantMenuItem(String tenantName) {
        JMenuItem mi = new JMenuItem(tenantName);
        mi.setActionCommand(CMD_SWITCH_TENANT);
        mi.addActionListener(this);
        return mi;
    }

    JPanel createMainPanel() {
        JComponent detail = createDetailPane();

        // 左右两部分合在一起，暂时不设置右边，后面会不断切换
        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, detail, null);
        mainPane.setOneTouchExpandable(true);
        mainPane.setDividerLocation(200);
        mainPane.setDividerSize(6);
        mainPane.setBorder(null);

        // 主窗口
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainPane, BorderLayout.CENTER);
        return mainPanel;
    }

    static JComponent createDetailPane() {
        return new JPanel();
    }

    static JFrame createMainFrame(JMenuBar menuBar, JPanel mainPanel) {
        // 主程序框架
        JFrame frame = new JFrame();
        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.setSize(870, 640);
        return frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (CMD_SWITCH_TENANT.equals(e.getActionCommand())) {
            if (e.getSource() instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) e.getSource();
                switchTenant(mi.getText());
            }
        }
    }
}
