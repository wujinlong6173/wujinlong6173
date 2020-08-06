package wjl.client;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseListener extends MouseAdapter implements MouseWheelListener {
    private final mxGraphComponent component;
    
    public MyMouseListener(mxGraphComponent component) {
        this.component = component;
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
            Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component);
            TopoPopupMenu menu = new TopoPopupMenu(component);
            menu.show(component, pt.x, pt.y);
            e.consume();
        }
    }
}
