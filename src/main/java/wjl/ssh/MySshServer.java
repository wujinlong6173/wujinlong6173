package wjl.ssh;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import wjl.cli.CommandViewFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用org.apache.sshd搭建的服务器，只利用其用户名和密码的特性。
 * 关键设计：不同的用户名登录到不同的虚拟路由器。
 *
 * 客户端连接 putty.exe -ssh -l user -pw password 127.0.0.1 22
 * 客户端显示乱码，Putty窗口 > Change Setting > Window > Transaction
 * > Remote character set > use font encoding.
 */
public class MySshServer {
    // 键值为TCP端口号
    private static final Map<Integer, MySshServer> SERVERS = new HashMap<>();
    private MySshShellFactory sshShellFactory;
    private CombPasswordAuthenticator authenticator;

    public MySshServer() {
        sshShellFactory = new MySshShellFactory();
        authenticator = new CombPasswordAuthenticator();
    }

    /**
     * 在指定的端口上启动SSH服务，或者给已启动的服务添加鉴权器、和命令视图工厂。
     *
     * @param port TCP端口号
     * @param authenticator 鉴权器
     * @param viewFactory 命令视图工厂
     */
    public static void start(int port, PasswordAuthenticator authenticator, CommandViewFactory viewFactory) {
        MySshServer myServer = SERVERS.get(port);
        if (myServer == null) {
            myServer = new MySshServer();
            try {
                SshServer ss = SshServer.setUpDefaultServer();
                ss.setHost("127.0.0.1");
                ss.setPort(port);
                ss.setPasswordAuthenticator(myServer.authenticator);
                ss.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
                ss.setShellFactory(myServer.sshShellFactory);
                ss.start(); // 本函数会立即返回
                SERVERS.put(port, myServer);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        myServer.authenticator.addMember(authenticator);
        myServer.sshShellFactory.addCommandViewFactory(viewFactory);
    }
}
