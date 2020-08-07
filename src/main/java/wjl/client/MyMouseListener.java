package wjl.client;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;

import wjl.client.ctrl.ClientControlCenter;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseListener extends MouseAdapter implements MouseWheelListener {
    private final ClientControlCenter ccc;
    private final mxGraphComponent component;
    private final JPopupMenu popupMenu;
    
    public MyMouseListener(mxGraphComponent component) {
        this.component = component;
        this.popupMenu = new JPopupMenu();
        this.ccc = new ClientControlCenter(component.getGraph(), popupMenu);
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                component.zoomIn();
            }
            else {
                component.zoomOut();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            mxPoint loc = component.getPointForEvent(e);
            ccc.setMousePosition(loc.getX(), loc.getY());
            
            Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component);
            ccc.refreshMenuState();
            popupMenu.show(component, pt.x, pt.y);
            e.consume();
        }
    }
}
