package wjl.telnets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MyTelnetThread extends Thread {
    private final Socket cs;
    private BufferedWriter writer;
    private InputStream in;

    public MyTelnetThread(Socket cs) {
        this.cs = cs;
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
                writer.write("Get ");
                writer.write(cmd);
                writer.write("\r\n>");
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
}
