package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * 在选中的设备上创建端口
 */
class CreatePortAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private static final int PORT_SIZE = 16;
    private static final String STYLE_PORT = 
            "shape=image;image=/images/5-pin-connector-32.png;" +
            "labelPosition=middle;verticalAlign=top;" +
            "movable=false;"; 
    
    private final ClientControlCenter ccc;
    private int nextId;
    
    public CreatePortAction(ClientControlCenter ccc) {
        super("创建端口");
        this.ccc = ccc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        mxCell dev = new mxCell("P" + ++nextId,
                new mxGeometry(ccc.getMouseX(), ccc.getMouseY(), PORT_SIZE, PORT_SIZE),
                STYLE_PORT);
        dev.setVertex(true);
        ccc.getGraph().addCell(dev);
    }
}
