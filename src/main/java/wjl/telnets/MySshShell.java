package wjl.telnets;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;

public class MySshShell extends Thread implements Command {
    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private final ChannelSession channel;
    private boolean crlf;

    public MySshShell(ChannelSession channel) {
        this.channel = channel;
    }

    @Override
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void setErrorStream(OutputStream err) {
        this.err = err;
    }

    @Override
    public void setExitCallback(ExitCallback callback) {

    }

    @Override
    public void start(ChannelSession channel, Environment env) {
        // 必须启动一个新线程，否则无法接收客户端输入的数据
        this.start();
    }

    @Override
    public void destroy(ChannelSession channel) throws Exception {

    }

    @Override
    public void run() {
        try {
            while (!channel.isClosed()) {
                String cmd = readCommand();
                if (cmd == null) {
                    break;
                }
                if (cmd.isEmpty()) {
                    continue;
                }

                out.write(cmd.getBytes());
                out.write('\r');
                out.write('\n');
                out.flush();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private String readCommand() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int ch = in.read();

            switch (ch) {
                case -1:
                    return null;
                case '\r':
                case '\n':
                    if (!crlf) {
                        crlf = true;
                        out.write('\r');
                        out.write('\n');
                        return sb.toString();
                    }
                    break;
                default:
                    crlf = false;
                    sb.append((char)ch);
                    out.write(ch);
                    out.flush();
            }
        }
    }
}
