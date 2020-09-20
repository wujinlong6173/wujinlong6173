package wjl.demo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * 模拟一个运营商，每个对象实例代表一个运营商，管理一张网络。
 * 实际应用时，每个运营商都部署一套L3NMS系统。
 */
public class SingleIspMgrDemo extends AbstractAction {
    private final String ispName;
    private final Properties cfg;

    /**
     *
     * @param ispName 运营商的名称
     */
    public SingleIspMgrDemo(String ispName) {
        super(ispName);
        this.ispName = ispName;
        this.cfg = loadConfig(String.format(Locale.ENGLISH, "/demo/%s.properties", ispName));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private Properties loadConfig(String fileName) {
        Properties cfg = new Properties();
        try (InputStream isCfg = SingleIspMgrDemo.class.getResourceAsStream(fileName)) {
            cfg.load(isCfg);
        } catch (IOException err) {
            err.printStackTrace();
        }
        return cfg;
    }
}
