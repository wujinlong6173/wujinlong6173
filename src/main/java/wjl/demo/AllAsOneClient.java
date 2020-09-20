package wjl.demo;

import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
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
        JMenu isp = new JMenu("运营商");
        isp.add(new SingleIspMgrDemo("移动"));
        isp.add(new SingleIspMgrDemo("电信"));
        isp.add(new CrossIspMgrDemo("互联互通"));

        JMenu tenant = new JMenu("租户");
        tenant.add(new TenantNetMgrDemo("租户A"));
        tenant.add(new TenantNetMgrDemo("租户B"));
        tenant.add(new TenantNetMgrDemo("租户C"));

        JMenuBar bar = new JMenuBar();
        bar.add(isp);
        bar.add(tenant);
        return bar;
    }

    static JPanel createMainPanel() {
        // 主窗口
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        //mainPanel.add(mainPane, BorderLayout.CENTER);
        return mainPanel;
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
