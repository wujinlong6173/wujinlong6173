package wjl.client.topo;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import wjl.client.mxgraph.mxCellDevice;
import wjl.client.mxgraph.mxCellLink;
import wjl.client.mxgraph.mxGraphBuilder;
import wjl.net.NetworkApi;

import javax.swing.*;
import java.awt.*;

public class TopoControlCenter {
    private final NetworkApi net;
    private final mxGraph graph;
    private final mxGraphComponent component;

    private final MyMouseListener mouse;
    private double mouseX;
    private double mouseY;

    private final JPopupMenu popupMenu;
    private final JMenuItem createDeviceMenu;
    private final JMenuItem createPortMenu;
    private final JMenuItem deleteMenu;
    private final JMenuItem deployMenu;
    private final JMenuItem undeployMenu;
    private final JMenuItem configMenu;

    /**
     * 拓扑图控制中心
     *
     * @param net 如果为空则自动创建一个
     */
    public TopoControlCenter(NetworkApi net) {
        // 初始化mxGraph组件
        if (net == null) {
            this.net = new NetworkApi();
            this.graph = new mxGraph();
        } else {
            this.net = net;
            this.graph = mxGraphBuilder.build(net);
        }
        component = new mxGraphComponent(graph);
        initGraphStyle();

        // 初始化右键菜单
        popupMenu = new JPopupMenu();
        createDeviceMenu = popupMenu.add(new CreateDeviceAction(this));
        createPortMenu = popupMenu.add(new CreatePortAction(this));
        popupMenu.add(new CreateLinkAction(this));
        deleteMenu = popupMenu.add(new DeleteAction(this));
        deployMenu = popupMenu.add(new DeployAction(this, true));
        undeployMenu = popupMenu.add(new DeployAction(this, false));
        configMenu = popupMenu.add(new ConfigDeviceAction(this));

        mouse = new MyMouseListener(component, this);
        // 通过Ctrl+鼠标滚轮控制缩放
        component.addMouseWheelListener(mouse);
        // 监听鼠标动作，显示右键菜单
        component.getGraphControl().addMouseListener(mouse);
    }

    private void initGraphStyle() {
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

        // 不需要拖动方式生成连线
        component.getConnectionHandler().setEnabled(false);
        // 设置背景颜色
        component.getViewport().setBackground(Color.WHITE);
        // 消除设备图标上面的加号，即展开、收缩的按钮
        component.setFoldingEnabled(false);
        component.setKeepSelectionVisibleOnZoom(true);
        component.setAutoExtend(true);
        component.setWheelScrollingEnabled(true);
    }

    public mxGraph getGraph() {
        return graph;
    }

    public NetworkApi getNet() {
        return net;
    }

    public JComponent getComponent() {
        return component;
    }

    void setMousePosition(mxPoint loc) {
        this.mouseX = loc.getX();
        this.mouseY = loc.getY();
    }

    double getMouseX() {
        return mouseX;
    }

    double getMouseY() {
        return mouseY;
    }

    void popupMenu(int x, int y) {
        popupMenu.show(component, x, y);
    }

    void refreshMenuState() {
        boolean selectEmpty = graph.isSelectionEmpty();
        createDeviceMenu.setEnabled(selectEmpty);
        deleteMenu.setEnabled(!selectEmpty);

        Object[] selected = graph.getSelectionCells();
        if (selected.length == 1) {
            if (selected[0] instanceof mxCellDevice) {
                // 选中一个网元时使能的菜单
                createPortMenu.setEnabled(true);
                deployMenu.setEnabled(true);
                undeployMenu.setEnabled(true);
                configMenu.setEnabled(true);
                return;
            } else if (selected[0] instanceof mxCellLink) {
                // 选中一条链路时使能的菜单
                createPortMenu.setEnabled(false);
                deployMenu.setEnabled(true);
                undeployMenu.setEnabled(true);
                configMenu.setEnabled(false);
                return;
            }
        }

        createPortMenu.setEnabled(false);
        deployMenu.setEnabled(false);
        undeployMenu.setEnabled(false);
        configMenu.setEnabled(false);
    }

    /**
     * 只有选中单个设备时，才返回该设备，否则返回空
     *
     * @return 选中的设备或空
     */
    mxCellDevice getSelectedDevice() {
        Object[] selected = graph.getSelectionCells();
        if (selected.length == 1) {
            if (selected[0] instanceof mxCellDevice) {
                return (mxCellDevice)selected[0];
            }
        }
        return null;
    }

    /**
     * 只有选中单条链路时，才返回该链路，否则返回空
     *
     * @return 选中的链路或空
     */
    mxCellLink getSelectedLink() {
        Object[] selected = graph.getSelectionCells();
        if (selected.length == 1) {
            if (selected[0] instanceof mxCellLink) {
                return (mxCellLink)selected[0];
            }
        }
        return null;
    }
}
