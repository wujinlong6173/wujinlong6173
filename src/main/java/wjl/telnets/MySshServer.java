package wjl.telnets;

import com.huawei.cli.HuaWeiSystem;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import java.io.IOException;

/**
 * 使用org.apache.sshd搭建的服务器，只利用其用户名和密码的特性。
 * 关键设计：不同的用户名登录到不同的虚拟路由器。
 *
 * 客户端连接 putty.exe -ssh -l user -pw password 127.0.0.1 22
 * 客户端显示乱码，Putty窗口 > Change Setting > Window > Transaction
 * > Remote character set > use font encoding.
 */
public class MySshServer {
    public static void main(String[] args) throws InterruptedException {
        start(22);
        System.out.println("SSH Server 22 is running...");
        // 让主线程一直等着
        Object dead = new Object();
        synchronized (dead) {
            dead.wait();
        }
    }

    public static void start(int port) {
        try {
            SshServer ss = SshServer.setUpDefaultServer();
            ss.setHost("127.0.0.1");
            ss.setPort(port);
            ss.setPasswordAuthenticator(new HuaWeiSystem());
            ss.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
            //ss.setShellFactory(winShellFactory());
            ss.setShellFactory(new MySshShellFactory());
            ss.start(); // 本函数会立即返回
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //static ShellFactory winShellFactory() {
    //return new ProcessShellFactory("cmd.exe", "Echo", "ICrN1", "ON1Cr");
    //}
}
