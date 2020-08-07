package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * 删除选中的对象
 */
class DeleteAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    private final ClientControlCenter ccc;
    
    public DeleteAction(ClientControlCenter ccc) {
        super("删除");
        this.ccc = ccc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ccc.getGraph().removeCells();
    }
}
