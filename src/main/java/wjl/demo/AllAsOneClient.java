package wjl.demo;

import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import wjl.client.TabbedToolsPane;
import wjl.client.topo.TopoControlCenter;
import wjl.provider.ProviderMgr;
import wjl.util.Config;

import javax.swing.*;
import java.awt.*;

/**
 * 所有东西放在一个进程中运行，提供一个界面。
 */
public class AllAsOneClient {
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

        JMenuBar menuBar = createMainMenu();
        JPanel mainPanel = createMainPanel();
        JFrame mainFrame = createMainFrame(menuBar, mainPanel);
        mainFrame.setTitle("L3NMS");
        mainFrame.setVisible(true);
    }

    static JMenuBar createMainMenu() {
        ProviderMgr forTenant = new ProviderMgr();
        TenantNetMgrDemo ta = new TenantNetMgrDemo("租户A", forTenant);
        TenantNetMgrDemo tb = new TenantNetMgrDemo("租户B", forTenant);
        TenantNetMgrDemo tc = new TenantNetMgrDemo("租户C", forTenant);

        ProviderMgr forMobile = new ProviderMgr();
        SingleIspMgrDemo ia = new SingleIspMgrDemo("移动", forMobile);

        ProviderMgr forTel = new ProviderMgr();
        SingleIspMgrDemo ib = new SingleIspMgrDemo("电信", forTel);

        JMenu isp = new JMenu("运营商");
        isp.add(new ShowFrameAction(ia.getIspName(), ia));
        isp.add(new ShowFrameAction(ib.getIspName(), ib));
        isp.add(new CrossIspMgrDemo("互联互通"));

        JMenu tenant = new JMenu("租户");
        tenant.add(new ShowFrameAction(ta.getTenantName(), ta));
        tenant.add(new ShowFrameAction(tb.getTenantName(), tb));
        tenant.add(new ShowFrameAction(tc.getTenantName(), tc));

        JMenuBar bar = new JMenuBar();
        bar.add(isp);
        bar.add(tenant);
        return bar;
    }

    static JPanel createMainPanel() {
        JComponent tools = createToolsPane();
        JComponent detail = createDetailPane();
        TopoControlCenter ccc = new TopoControlCenter(null, null);

        // 左边的面板
        JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tools, detail);
        leftPane.setDividerLocation(320);
        leftPane.setResizeWeight(1);
        leftPane.setDividerSize(6);
        leftPane.setBorder(null);

        // 左右两部分合在一起
        JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, ccc.getComponent());
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

    static JComponent createToolsPane() {
        // 创建网络的工具栏，分成多个Tab页
        TabbedToolsPane tools = new TabbedToolsPane();
        tools.add("Device", "/client/device_templates.yaml");
        tools.add( "BGP", "/client/bgp_templates.yaml");
        return tools;
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
}
