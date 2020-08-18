package wjl.telnets;

import com.huawei.cli.SystemView;
import wjl.cli.CommandHandler;
import wjl.cli.CommandHandlers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 非常简单的Telnet服务器，没有任何安全措施。
 */
public class TelnetServer {
    public static void main(String[] args) {
        start(23);
    }

    public static void start(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket cs = ss.accept();
                // TODO 不应该反向依赖com.huawei中的内容
                CommandHandlers handlers = new CommandHandlers(
                        new CommandHandler("System", new SystemView()));
                MyTelnetThread thread = new MyTelnetThread(cs, handlers);
                thread.start();
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
