package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;

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
        mxCell dev = new mxCellDevice("Router" + ++nextId, ccc.getMouseX(), ccc.getMouseY());
        ccc.getGraph().addCell(dev);
    }
}