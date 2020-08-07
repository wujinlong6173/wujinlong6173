package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

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

    }
}