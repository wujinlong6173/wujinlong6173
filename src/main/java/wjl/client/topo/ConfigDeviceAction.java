package wjl.client.topo;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import wjl.client.mxgraph.mxCellDevice;
import wjl.util.Config;
import wjl.util.ErrorType;

/**
 * 打开设备的配置终端
 */
class ConfigDeviceAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final TopoControlCenter ccc;
    
    public ConfigDeviceAction(TopoControlCenter ccc) {
        super("打开配置终端");
        this.ccc = ccc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        mxCellDevice dev = ccc.getSelectedDevice();
        if (dev == null) {
            JOptionPane.showMessageDialog(null, "选中一台设备，然后打开配置中的", 
                    ErrorType.OPER_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String telnetClient = Config.get("telnet.client");
        if (telnetClient == null) {
            JOptionPane.showMessageDialog(null, "没有配置Telnet客户端程序的路径", 
                    ErrorType.SYSTEM_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File tcFile = new File(telnetClient);
        if (!tcFile.isFile()) {
            JOptionPane.showMessageDialog(null, "配置的Telnet客户端程序不存在", 
                    ErrorType.SYSTEM_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String cfgEntry = ccc.getNet().getDeviceConfigEntry(dev.getId());
            if (cfgEntry != null) {
                String cmd = telnetClient + " " + cfgEntry;
                Runtime.getRuntime().exec(cmd);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, e1.getMessage(), 
                    ErrorType.SYSTEM_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }
}