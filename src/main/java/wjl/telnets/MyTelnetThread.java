package wjl.telnets;

import wjl.cli.CommandHandlers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class MyTelnetThread extends Thread {
    private final Socket cs;
    private final CommandHandlers handlers;
    private BufferedWriter writer;
    private InputStream in;

    public MyTelnetThread(Socket cs, CommandHandlers handlers) {
        this.cs = cs;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        try {
            in = cs.getInputStream();
            writer = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream()));
            writer.write(">");
            writer.flush();
            String cmd;
            while (cs.isConnected()) {
                cmd = readCommand();
                if (cmd.isEmpty()) {
                    break;
                }
                handleCommand(cmd);
                writer.write(">");
                writer.flush();
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private String readCommand() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (cs.isConnected()) {
            int ch = in.read();
            if (ch == TelnetProtocol.IAC) {
                TelnetProtocol.skipTelnetOption(in);
                continue;
            }

            switch (ch) {
                case '\n':
                case -1:
                    return sb.toString();
                case '\r':
                    break;
                default:
                    sb.append((char)ch);
            }
        }

        return sb.toString();
    }

    private void handleCommand(String cmd) throws IOException {
        List<String> msg = handlers.handle(cmd);
        if (msg != null) {
            for (String eachLine : msg) {
                writer.write(eachLine);
                writer.write("\r\n");
            }
        }
    }
}
