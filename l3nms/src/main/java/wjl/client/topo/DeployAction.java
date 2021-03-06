package wjl.client.topo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import wjl.client.mxgraph.mxCellDevice;
import wjl.client.mxgraph.mxCellLink;
import wjl.net.NetworkException;
import wjl.provider.DeviceProvider;
import wjl.provider.ProviderException;
import wjl.util.ErrorType;

/**
 * 部署选中的设备或链路
 */
class DeployAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final TopoControlCenter ccc;
    private final boolean deploy;
    private final InputDeployParamDialog deviceDlg;
    private final InputDeployParamDialog linkDlg;
    
    public DeployAction(TopoControlCenter ccc, boolean deploy) {
        super(deploy ? "部署" : "去部署");
        this.ccc = ccc;
        this.deploy = deploy;

        deviceDlg = new InputDeployParamDialog();
        linkDlg = new InputDeployParamDialog();
        deviceDlg.setProviders(ccc.getProductMgr().listDeviceProviders());
        linkDlg.setProviders(ccc.getProductMgr().listLinkProviders());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mxCellDevice dev = ccc.getSelectedDevice();
        if (dev != null) {
            if (deploy) deployDevice(dev);
            else undeployDevice(dev);
            return;
        }
        
        mxCellLink link = ccc.getSelectedLink();
        if (link != null) {
            if (deploy) deployLink(link);
            else undeployLink(link);
            return;
        }
        
        JOptionPane.showMessageDialog(null, "每次只能部署一台设备或一条链路", 
                ErrorType.SYSTEM_LIMMIT.getDesc(), JOptionPane.ERROR_MESSAGE);
    }

    private void deployDevice(mxCellDevice dev) {
        if (!deviceDlg.acquireInputs()) {
            return;
        }
        
        try {
            String providerName = deviceDlg.getSelectedProvider();
            DeviceProvider provider = ccc.getProductMgr().getDeviceProvider(providerName);
            ccc.getNet().deployDevice(dev.getId(), providerName, deviceDlg.getInputs());
            dev.changeDeployState(deploy, provider.getIcon());
            ccc.getGraph().refresh();
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        } catch (ProviderException e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage(), 
                    e2.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void undeployDevice(mxCellDevice dev) {
        try {
            ccc.getNet().undeployDevice(dev.getId());
            dev.changeDeployState(deploy, null);
            ccc.getGraph().refresh();
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        } catch (ProviderException e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage(), 
                    e2.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deployLink(mxCellLink link) {
        if (!linkDlg.acquireInputs()) {
            return;
        }

        try {
            ccc.getNet().deployLink(link.getId(), linkDlg.getSelectedProvider(), linkDlg.getInputs());
            link.changeDeployState(deploy);
            ccc.getGraph().refresh();
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        } catch (ProviderException e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage(), 
                    e2.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void undeployLink(mxCellLink link) {
        try {
            ccc.getNet().undeployLink(link.getId());
            link.changeDeployState(deploy);
            ccc.getGraph().refresh();
        } catch (NetworkException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    e1.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        } catch (ProviderException e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage(), 
                    e2.getErrorType().getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
