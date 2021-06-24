package wjl.client.topo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.model.mxCell;

import wjl.client.mxgraph.mxCellDevice;
import wjl.client.mxgraph.mxCellPort;
import wjl.net.NetworkException;
import wjl.util.ErrorType;

/**
 * 在选中的设备上创建端口
 */
class CreatePortAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final TopoControlCenter ccc;
    
    public CreatePortAction(TopoControlCenter ccc) {
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

        int dir = mxCellDevice.position(ccc.getMouseX() - dev.getCenterX(), ccc.getMouseY() - dev.getCenterY());
        dir = dev.emptyPosition(dir);
        if (dir < 0) {
            JOptionPane.showMessageDialog(null, "每个设备最多八个端口", 
                    ErrorType.SYSTEM_LIMMIT.getDesc(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String portName = "P" + dir;
            mxCell port = new mxCellPort(portName,
                    mxCellDevice.getPositionX(dir), mxCellDevice.getPositionY(dir));
            String portId = ccc.getNet().createPort(dev.getId(), portName, null);
            port.setId(portId);
            ccc.getGraph().addCell(port, dev);
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }

}
