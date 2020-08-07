package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.model.mxCell;

/**
 * 在选中的设备上创建端口
 */
class CreatePortAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private static final int POSITION[][];
    
    static {
        int d = mxCellDevice.DEVICE_SIZE / 2;
        int r = (mxCellDevice.DEVICE_SIZE - mxCellPort.PORT_SIZE) / 2;
        POSITION = new int[8][];
        POSITION[0] = new int[] {d, d-r};
        POSITION[1] = new int[] {d+r, d-r};
        POSITION[2] = new int[] {d+r, d};
        POSITION[3] = new int[] {d+r, d+r};
        POSITION[4] = new int[] {d, d+r};
        POSITION[5] = new int[] {d-r, d+r};
        POSITION[6] = new int[] {d-r, d};
        POSITION[7] = new int[] {d-r, d-r};
    }
    
    private final ClientControlCenter ccc;
    private int nextId;
    
    public CreatePortAction(ClientControlCenter ccc) {
        super("创建端口");
        this.ccc = ccc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        mxCellDevice dev = ccc.getSelectedDevice();
        if (dev == null) {
            JOptionPane.showMessageDialog(null, "选中单个设备才能创建端口", "操作错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
     
        if (dev.getChildCount() >= 8) {
            JOptionPane.showMessageDialog(null, "每个设备最多八个端口", "系统局限", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int dir = angle(ccc.getMouseX() - dev.getCenterX(), ccc.getMouseY() - dev.getCenterY());

        mxCell port = new mxCellPort("P" + ++nextId, POSITION[dir][0], POSITION[dir][1]);
        ccc.getGraph().addCell(port, dev);
    }

    // 从顶部算起，逆时针顺序，依次编号为0~7
    static int angle(double dx, double dy) {
        double angle = Math.atan(dy / dx);
        angle += Math.PI * 5 / 8;
        int dir = (int)Math.floor(angle * 4.0 / Math.PI);
        if (dx < 0) {
            dir = (dir + 4) % 8;
        }
        return dir;
    }
}
