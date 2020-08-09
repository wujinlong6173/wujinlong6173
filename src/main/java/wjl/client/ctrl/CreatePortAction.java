package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;

import wjl.net.NetworkException;
import wjl.util.ErrorType;

/**
 * 在选中的设备上创建端口
 */
class CreatePortAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private static final double POSITION[][];
    private static final int MAX_PORT = 8;
    private static final int XY_SHIFT = mxCellDevice.DEVICE_SIZE / 2;
    
    static {
        int r = (mxCellDevice.DEVICE_SIZE - mxCellPort.PORT_SIZE) / 2;
        double r2 = r * Math.sqrt(0.5);
        POSITION = new double[MAX_PORT][];
        POSITION[0] = new double[] {XY_SHIFT,    XY_SHIFT-r};
        POSITION[1] = new double[] {XY_SHIFT+r2, XY_SHIFT-r2};
        POSITION[2] = new double[] {XY_SHIFT+r,  XY_SHIFT};
        POSITION[3] = new double[] {XY_SHIFT+r2, XY_SHIFT+r2};
        POSITION[4] = new double[] {XY_SHIFT,    XY_SHIFT+r};
        POSITION[5] = new double[] {XY_SHIFT-r2, XY_SHIFT+r2};
        POSITION[6] = new double[] {XY_SHIFT-r,  XY_SHIFT};
        POSITION[7] = new double[] {XY_SHIFT-r2, XY_SHIFT-r2};
    }
    
    private final ClientControlCenter ccc;
    
    public CreatePortAction(ClientControlCenter ccc) {
        super("创建端口");
        this.ccc = ccc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        mxCellDevice dev = ccc.getSelectedDevice();
        if (dev == null) {
            JOptionPane.showMessageDialog(null, "选中单个设备才能创建端口",
                    ErrorType.OPER_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        int dir = position(ccc.getMouseX() - dev.getCenterX(), ccc.getMouseY() - dev.getCenterY());
        dir = emptyPosition(dir, dev);
        if (dir < 0) {
            JOptionPane.showMessageDialog(null, "每个设备最多八个端口", 
                    ErrorType.SYSTEM_LIMMIT.getDesc(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String portName = "P" + dir;
            mxCell port = new mxCellPort(portName, POSITION[dir][0], POSITION[dir][1]);
            String portId = ccc.getNet().createPort(dev.getId(), portName, null);
            port.setId(portId);
            ccc.getGraph().addCell(port, dev);
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }

    // 从顶部算起，顺时针顺序，依次编号为0~7
    static int position(double dx, double dy) {
        double angle = Math.atan(dy / dx);
        angle += Math.PI * 5 / 8;
        int dir = (int)Math.floor(angle * 4.0 / Math.PI);
        if (dx < 0) {
            dir = (dir + 4) % 8;
        }
        return dir;
    }
    
    static int emptyPosition(int start, mxCellDevice dev) {
        if (dev.getChildCount() >= MAX_PORT) {
            return -1;
        }
        
        int idx;
        boolean[] flags = new boolean[MAX_PORT];
        for (idx = 0; idx < dev.getChildCount(); idx++) {
            mxICell child = dev.getChildAt(idx);
            int p = position(child.getGeometry().getCenterX() - XY_SHIFT,
                    child.getGeometry().getCenterY() - XY_SHIFT);
            flags[p] = true;
        }
        
        for (idx = 0; idx < MAX_PORT; idx++) {
            if (!flags[(start + idx) % MAX_PORT]) {
                return (start + idx) % MAX_PORT;
            }
        }
        return -1;
    }
}
