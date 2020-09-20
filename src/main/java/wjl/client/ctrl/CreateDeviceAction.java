package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;
import wjl.client.mxgraph.mxCellDevice;

/**
 * 创建设备
 */
class CreateDeviceAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final ClientControlCenter ccc;
    private int nextId;
    
    public CreateDeviceAction(ClientControlCenter ccc) {
        super("创建设备");
        this.ccc = ccc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String devName = "Router" + ++nextId;
        mxCell dev = new mxCellDevice(devName, ccc.getMouseX(), ccc.getMouseY());
        String devId = ccc.getNet().createDevice(devName);
        dev.setId(devId);
        ccc.getGraph().addCell(dev);
    }
}