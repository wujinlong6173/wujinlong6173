package wjl.demo;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 模拟多个运营商互联互通，可以理解为一个新的运营商，它自己没有路由器，
 * 单有很多纤缆，用于连接其它运营商的路由器。
 */
public class CrossIspMgrDemo extends AbstractAction {
    private String ispName;

    public CrossIspMgrDemo(String ispName) {
        super(ispName);
        this.ispName = ispName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
