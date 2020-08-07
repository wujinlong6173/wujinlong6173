package wjl.client.ctrl;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.mxgraph.view.mxGraph;

public class ClientControlCenter {
    private final mxGraph graph;
    
    private final JMenuItem createDeviceMenu;
    private final JMenuItem createPortMenu;
    private final JMenuItem createLinkMenu;
    private final JMenuItem deleteMenu;
    private final JMenuItem deployMenu;
    private final JMenuItem undeployMenu;
    private final JMenuItem configMenu;
    
    private double mouseX;
    private double mouseY;

    public ClientControlCenter(mxGraph graph, JPopupMenu popupMenu) {
        this.graph = graph;
        
        createDeviceMenu = popupMenu.add(new CreateDeviceAction(this));
        createPortMenu = popupMenu.add(new CreatePortAction(this));
        createLinkMenu = popupMenu.add(new CreateLinkAction(this));
        deleteMenu = popupMenu.add(new DeleteAction(this));
        deployMenu = popupMenu.add(new DeployAction());
        undeployMenu = popupMenu.add(new UnDeployAction());
        configMenu = popupMenu.add(new ConfigDeviceAction());
    }
    
    public void refreshMenuState() {
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
            }
        }
        
        createPortMenu.setEnabled(false);
        deployMenu.setEnabled(false);
        undeployMenu.setEnabled(false);
        configMenu.setEnabled(false);
    }

    public void setMousePosition(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public mxGraph getGraph() {
        return graph;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
    
    /**
     * 只有选中单个设备时，才返回该设备，否则返回空
     * 
     * @return 选中的设备或空
     */
    public mxCellDevice getSelectedDevice() {
        Object[] selected = graph.getSelectionCells();
        if (selected.length == 1) {
            if (selected[0] instanceof mxCellDevice) {
                return (mxCellDevice)selected[0];
            }
        }
        return null;
    }
}
