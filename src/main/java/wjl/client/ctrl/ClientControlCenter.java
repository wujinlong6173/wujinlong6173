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
    
    private int mouseX;
    private int mouseY;

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
    }

    public void setMousePosition(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public mxGraph getGraph() {
        return graph;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
