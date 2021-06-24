package wjl.client.ctrl;

import wjl.util.Config;
import wjl.util.ErrorType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * 打开SSH终端，管理、查看物理网络。
 */
public class HuaWeiAdminAction extends AbstractAction {
    public HuaWeiAdminAction() {
        super("打开管理终端");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            String cmd = telnetClient + " -ssh -l admin -pw 123456 127.0.0.1 22";
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, e1.getMessage(),
                    ErrorType.SYSTEM_ERROR.getDesc(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
