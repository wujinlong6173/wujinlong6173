package wjl.client.topo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;
import wjl.client.mxgraph.mxCellDevice;

/**
 * 创建设备
 */
class CreateDeviceAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final TopoControlCenter ccc;
    private int nextId;
    
    public CreateDeviceAction(TopoControlCenter ccc) {
        super("创建设备");
        this.ccc = ccc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String devName = "Router" + ++nextId;
        mxCell dev = new mxCellDevice(devName, ccc.getMouseX(), ccc.getMouseY());
        String devId = ccc.getNet().createDevice(devName);
        ccc.getNet().setDevicePosition(devId, ccc.getMouseX(), ccc.getMouseY());
        dev.setId(devId);
        ccc.getGraph().addCell(dev);
    }
}