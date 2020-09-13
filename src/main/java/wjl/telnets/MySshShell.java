package wjl.telnets;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import wjl.cli.CommandHandlers;

import java.io.*;
import java.util.List;

public class MySshShell extends Thread implements Command {
    private static byte[] CR_LF = "\r\n".getBytes();
    private static String BYE_BYE = "Bye bye !";

    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private final ChannelSession channel;
    private boolean crlf;

    // 命令行处理器
    private final CommandHandlers handlers;

    public MySshShell(ChannelSession channel, CommandHandlers handlers) {
        this.channel = channel;
        this.handlers = handlers;
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
                // 输出命令提示符
                out.write(handlers.getPrompt().getBytes());
                out.flush();

                // 读取命令
                String cmd = readCommand();
                if (cmd == null) {
                    break;
                }
                if (cmd.isEmpty()) {
                    continue;
                }

                handleCommand(cmd);
                if (handlers.isEmpty()) {
                    break;
                }
            }

            out.write(BYE_BYE.getBytes());
            out.flush();
            channel.getSession().disconnect(11, BYE_BYE);
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
                case 27: // 忽略ESC和其它控制字符
                    in.skip(in.available());
                    break;
                case 127: // 退格键
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                        out.write(ch);
                        out.flush();
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

    private void handleCommand(String cmd) throws IOException {
        List<String> msg = handlers.handle(cmd);
        if (msg != null) {
            for (String eachLine : msg) {
                out.write(eachLine.getBytes());
                out.write(CR_LF);
            }
        }
    }
}
