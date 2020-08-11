package wjl.client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
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

        JMenuBar menuBar = new JMenuBar();
        
        mxGraph graph = new mxGraph();
        JComponent tools = createToolsPane();
        JComponent detail = createDetailPane();
        JComponent topo = createTopoPane(graph);
        JFrame mainFrame = createMainFrame(menuBar, tools, detail, topo);
        mainFrame.setTitle("L3NMS");
        mainFrame.setVisible(true);
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
    
    static JComponent createTopoPane(mxGraph graph) {
        // 从文件加载图数据后不改变缩放比例
        graph.setResetViewOnRootChange(false);
        // 拖拽节点到边的中间时，不允许自动分割边
        graph.setSplitEnabled(false);
        // 拖拽边时，不会解除和节点的绑定，只有拖拽端点才能解除绑定
        graph.setDisconnectOnMove(false);
        // 禁止改变图标的大小
        graph.setCellsResizable(false);
        // 将一个阶段拖放到另一个节点时，不自动调整后者的大小
        graph.setExtendParentsOnAdd(false);
        // 禁止拖动边的端点，避免和节点解除绑定
        graph.setCellsDisconnectable(false);
        // 允许选中多个对象
        graph.setCellsSelectable(true);

        mxGraphComponent component = new mxGraphComponent(graph);
        // 不需要拖动方式生成连线
        component.getConnectionHandler().setEnabled(false);
        // 设置背景颜色
        component.getViewport().setBackground(Color.WHITE);
        
        MyMouseListener mouse = new MyMouseListener(component);
        // 通过Ctrl+鼠标滚轮控制缩放
        component.addMouseWheelListener(mouse);
        // 监听鼠标动作，显示右键菜单
        component.getGraphControl().addMouseListener(mouse);
        // 消除设备图标上面的加号，即展开、收缩的按钮
        component.setFoldingEnabled(false);
        component.setKeepSelectionVisibleOnZoom(true);
        component.setAutoExtend(true);
        component.setWheelScrollingEnabled(true);

        return component;
    }
}
