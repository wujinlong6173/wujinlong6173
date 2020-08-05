package wjl.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import wjl.util.YamlLoader;

public class NetworkTopoPanel extends JPanel {
    private static final long serialVersionUID = -3030196709511241783L;
    
    protected mxGraphComponent component;
    
    public NetworkTopoPanel(mxGraphComponent component) {
        this.component = component;

        setGraphViewStyle();
        installMouseListeners();
        
        // 创建网络的工具栏，分成多个Tab页
        JTabbedPane libraryPane = new JTabbedPane();
        initEditorPalette(insertPalette(libraryPane, "Device"), 
                "/client/device_templates.yaml");
        initEditorPalette(insertPalette(libraryPane, "BGP"),
                "/client/bgp_templates.yaml");
        
        // 显示选中对象的详细信息
        JPanel detailPane = new JPanel();
        
        // 左边的面板
        JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                libraryPane, detailPane);
        leftPane.setDividerLocation(320);
        leftPane.setResizeWeight(1);
        leftPane.setDividerSize(6);
        leftPane.setBorder(null);
        
        // 左右两部分合在一起
        JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                leftPane, component);
        outer.setOneTouchExpandable(true);
        outer.setDividerLocation(200);
        outer.setDividerSize(6);
        outer.setBorder(null);
        
        // Puts everything together
        setLayout(new BorderLayout());
        add(outer, BorderLayout.CENTER);
    }
    
    void setGraphViewStyle() {
        final mxGraph graph = component.getGraph();
        // 从文件加载图数据后不改变缩放比例
        graph.setResetViewOnRootChange(false);
        // 拖拽节点到边的中间时，不允许自动分割边
        graph.setSplitEnabled(false);
        // 拖拽边时，不会解除和节点的绑定，只有拖拽端点才能解除绑定
        graph.setDisconnectOnMove(false);
        
        // 模拟页面方式显示拓扑图
        //component.setPageVisible(true);
        // 不需要拖动方式生成连线
        component.getConnectionHandler().setEnabled(false);
        // 设置背景颜色
        component.getViewport().setBackground(Color.WHITE);
    }
    
    void installMouseListeners() {
        MouseWheelListener wheelTracker = new MouseWheelListener()
        {
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        component.zoomIn();
                    }
                    else {
                        component.zoomOut();
                    }
                }
            }
        };

        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                // Handles context menu on the Mac where the trigger is on mousepressed
                mouseReleased(e);
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    Point pt = SwingUtilities.convertPoint(
                            e.getComponent(), e.getPoint(), component);
                    TopoPopupMenu menu = new TopoPopupMenu(component);
                    menu.show(component, pt.x, pt.y);
                    e.consume();
                }
            }
        };
        
        // 通过Ctrl+鼠标滚轮控制缩放
        component.addMouseWheelListener(wheelTracker);
        // 监听鼠标动作，显示右键菜单
        component.getGraphControl().addMouseListener(mouseAdapter);
    }
    
    EditorPalette insertPalette(JTabbedPane parent, String title)
    {
        final EditorPalette palette = new EditorPalette();
        final JScrollPane scrollPane = new JScrollPane(palette);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        parent.add(title, scrollPane);

        // 跟随父面板改变工具盘的界面大小
        parent.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                int w = scrollPane.getWidth()
                        - scrollPane.getVerticalScrollBar().getWidth();
                palette.setPreferredWidth(w);
            }

        });

        return palette;
    }
    
    void initEditorPalette(EditorPalette palette, String filename) {
        EditorPaletteConf conf = YamlLoader.fileToObject(EditorPaletteConf.class, filename);
        if (conf == null) {
            return;
        }
        
        if (conf.nodes != null) {
            for (Object node : conf.nodes) {
                Map<String,Object> mapNode = (Map<String,Object>)node;
                String name = (String)mapNode.get("name");
                String icon = (String)mapNode.get("icon");
                URL iconUrl = NetworkTopoPanel.class.getResource(icon);
                if (iconUrl == null) {
                    continue;
                }
                
                Integer size = (Integer)mapNode.get("size");
                if (size == null) size = 50;
                Map<String,Object> style = (Map<String,Object>)mapNode.get("style");

                mxCell tpl = palette.addTemplate(name, new ImageIcon(iconUrl), 
                        styleString(style), size, size, name);
                if (Boolean.FALSE.equals(mapNode.get("connectable"))) {
                    tpl.setConnectable(false);
                }
            }
        }
        
        if (conf.edges != null) {
            for (Object edge : conf.edges) {
                Map<String,Object> mapEdge = (Map<String,Object>)edge;
                String name = (String)mapEdge.get("name");
                String icon = (String)mapEdge.get("icon");
                URL iconUrl = NetworkTopoPanel.class.getResource(icon);
                if (iconUrl == null) {
                    continue;
                }
                
                Map<String,Object> style = (Map<String,Object>)mapEdge.get("style");
                palette.addEdgeTemplate(name, new ImageIcon(iconUrl),
                        styleString(style), 80, 80, name);                
            }
        }
    }
    
    String styleString(Map<String,Object> style) {
        if (style == null || style.isEmpty()) {
            return null;
        }
        
        List<String> items = new ArrayList<String>(style.size());
        for (Map.Entry<String,Object> entry : style.entrySet()) {
            items.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return String.join(";", items);
    }
}
