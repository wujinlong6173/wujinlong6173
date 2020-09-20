package wjl.demo;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 模拟一个租户，每个对象实例代表一个租户，管理一张网络。
 */
public class TenantNetMgrDemo extends AbstractAction {
    private final String tenantName;

    /**
     *
     * @param tenantName 租户名称，唯一标识
     */
    public TenantNetMgrDemo(String tenantName) {
        super(tenantName);
        this.tenantName = tenantName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
