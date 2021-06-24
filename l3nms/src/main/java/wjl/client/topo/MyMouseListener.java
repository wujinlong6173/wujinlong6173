package wjl.client.topo;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MyMouseListener extends MouseAdapter implements MouseWheelListener {
    private final TopoControlCenter ccc;
    private final mxGraphComponent component;

    public MyMouseListener(mxGraphComponent component, TopoControlCenter ccc) {
        this.component = component;
        this.ccc = ccc;
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
            ccc.setMousePosition(loc);

            Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component);
            ccc.refreshMenuState();
            ccc.popupMenu(pt.x, pt.y);
            e.consume();
        }
    }
}
