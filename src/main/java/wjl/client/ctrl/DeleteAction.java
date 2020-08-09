package wjl.client.ctrl;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import wjl.net.NetworkException;

/**
 * 删除选中的对象
 */
class DeleteAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    private final ClientControlCenter ccc;
    private final List<Object> removedCells = new ArrayList<>();
    
    public DeleteAction(ClientControlCenter ccc) {
        super("删除");
        this.ccc = ccc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            removedCells.clear();
            Object[] cells = ccc.getGraph().getSelectionCells();
            // 删除一批对象时，必须按顺序删除
            deleteLinks(cells);
            deletePorts(cells);
            deleteDevices(cells);
            ccc.getGraph().removeCells();
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        } finally {
            ccc.getGraph().removeCells(removedCells.toArray());
        }
    }
    
    private void deleteLinks(Object[] cells) throws NetworkException {
        for (Object cell : cells) {
            if (!(cell instanceof mxCellLink)) {
                continue;
            }
            
            mxCellLink link = (mxCellLink)cell;
            ccc.getNet().deleteLink(link.getId());
            removedCells.add(link.getId());
        }
    }
    
    private void deletePorts(Object[] cells) throws NetworkException {
        for (Object cell : cells) {
            if (!(cell instanceof mxCellPort)) {
                continue;
            }
            
            mxCellPort port = (mxCellPort)cell;
            ccc.getNet().deletePort(port.getId());
            removedCells.add(port.getId());
        }
    }
    
    private void deleteDevices(Object[] cells) throws NetworkException {
        for (Object cell : cells) {
            if (!(cell instanceof mxCellDevice)) {
                continue;
            }
            
            mxCellDevice dev = (mxCellDevice)cell;
            ccc.getNet().deleteDevice(dev.getId());
            removedCells.add(dev.getId());
        }
    }
}
