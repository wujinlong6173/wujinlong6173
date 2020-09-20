package wjl.client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import com.huawei.inventory.HuaWeiInventory;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import wjl.client.ctrl.HuaWeiAdminAction;
import wjl.client.topo.TopoControlCenter;
import wjl.telnets.MySshServer;
import wjl.util.Config;

/**
 * 
 * JVM参数：
 * -Dfile.encoding=gbk
 */
public class ClientMain {

    public static void main(String[] args) {
        // 设置程序窗口的外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        Config.load();

        mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
        mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";

        JMenuBar menuBar = createMainMenu();

        JComponent tools = createToolsPane();
        JComponent detail = createDetailPane();
        JComponent topo = createTopoPane();
        JFrame mainFrame = createMainFrame(menuBar, tools, detail, topo);
        mainFrame.setTitle("L3NMS");
        mainFrame.setVisible(true);

        HuaWeiInventory.loadFromFile("/huawei/inventory.yaml");
        MySshServer.start(22);
    }
    
    static JFrame createMainFrame(JMenuBar menuBar, JComponent tools, JComponent detail, JComponent topo) {
        // 左边的面板
        JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tools, detail);
        leftPane.setDividerLocation(320);
        leftPane.setResizeWeight(1);
        leftPane.setDividerSize(6);
        leftPane.setBorder(null);
        
        // 左右两部分合在一起
        JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, topo);
        mainPane.setOneTouchExpandable(true);
        mainPane.setDividerLocation(200);
        mainPane.setDividerSize(6);
        mainPane.setBorder(null);

        // 主窗口
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainPane, BorderLayout.CENTER);
        
        // 主程序框架
        JFrame frame = new JFrame();
        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.setSize(870, 640);
        return frame;
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
    
    static JComponent createTopoPane() {
        TopoControlCenter ccc = new TopoControlCenter(null);
        return ccc.getComponent();
    }

    static JMenuBar createMainMenu() {
        JMenu hw = new JMenu("华为");
        hw.add(new HuaWeiAdminAction());

        JMenuBar bar = new JMenuBar();
        bar.add(hw);
        return bar;
    }
}
