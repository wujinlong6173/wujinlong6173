package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * 创建设备
 */
class CreateDeviceAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private static final int DEVICE_SIZE = 84;
    private static final String STYLE_DEVICE = 
            "shape=image;image=/images/router-60.png;" +
            "verticalLabelPosition=bottom;verticalAlign=top;";

    private final ClientControlCenter ccc;
    private int nextId;
    
    public CreateDeviceAction(ClientControlCenter ccc) {
        super("创建设备");
        this.ccc = ccc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        mxCell dev = new mxCell("Router" + ++nextId,
                new mxGeometry(ccc.getMouseX(), ccc.getMouseY(), DEVICE_SIZE, DEVICE_SIZE),
                STYLE_DEVICE);
        dev.setVertex(true);
        dev.setConnectable(false);
        ccc.getGraph().addCell(dev);
    }
}