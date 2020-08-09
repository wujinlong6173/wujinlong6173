package wjl.client.ctrl;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import wjl.client.dialog.InputDeployParamDialog;

/**
 * 部署选中的设备或链路
 */
class DeployAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final ClientControlCenter ccc;
    private final boolean deploy;
    private final InputDeployParamDialog deviceDlg = new InputDeployParamDialog();
    private final InputDeployParamDialog linkDlg = new InputDeployParamDialog();
    
    public DeployAction(ClientControlCenter ccc, boolean deploy) {
        super(deploy ? "部署" : "去部署");
        this.ccc = ccc;
        this.deploy = deploy;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        mxCellDevice dev = ccc.getSelectedDevice();
        if (dev != null) {
            deployDevice(dev);
            return;
        }
        
        mxCellLink link = ccc.getSelectedLink();
        if (link != null) {
            deployLink(link);
            return;
        }
        
        JOptionPane.showMessageDialog(null, "每次只能部署一台设备或一条链路", 
                ErrorMsg.SYSTEM_LIMMIT, JOptionPane.ERROR_MESSAGE);
    }

    private void deployDevice(mxCellDevice dev) {
        if (!deviceDlg.acquireInputs()) {
            return;
        }
        
        dev.changeDeployState(deploy);
        ccc.getGraph().refresh();
    }
    
    private void deployLink(mxCellLink link) {
        if (!linkDlg.acquireInputs()) {
            return;
        }
        
        link.changeDeployState(deploy);
        ccc.getGraph().refresh();
    }
}

