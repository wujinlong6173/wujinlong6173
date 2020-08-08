package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * 创建链路
 */
class CreateLinkAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final ClientControlCenter ccc;
    private int nextId;
    
    public CreateLinkAction(ClientControlCenter ccc) {
        super("创建链路");
        this.ccc = ccc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object[] cells = ccc.getGraph().getSelectionCells();
        if (!isOnlyTwoPort(cells)) {
            JOptionPane.showMessageDialog(null, "选中两个端口才能创建链路", 
                    ErrorMsg.OPER_ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        mxCellPort port1 = (mxCellPort)cells[0];
        mxCellPort port2 = (mxCellPort)cells[1];
        if (port1.getParent() == port2.getParent()) {
            JOptionPane.showMessageDialog(null, "不支持环回链路", 
                    ErrorMsg.SYSTEM_LIMMIT, JOptionPane.ERROR_MESSAGE);
            return;
        }

        mxCellLink link = new mxCellLink("Link" + ++nextId, port1, port2);
        ccc.getGraph().addEdge(link, null, port1, port2, null);
    }
    
    private boolean isOnlyTwoPort(Object[] cells) {
        if (cells.length != 2) {
            return false;
        }
        
        for (Object cell : cells) {
            if (!(cell instanceof mxCellPort)) {
                return false;
            }
        }
        
        return true;
    }
}