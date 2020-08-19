package wjl.telnets;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.IOException;

public class MySshShellFactory implements ShellFactory {
    @Override
    public Command createShell(ChannelSession channel) throws IOException {
        return new MySshShell(channel);
    }
}
