package wjl.telnets;

import com.huawei.cli.SystemView;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import wjl.cli.CommandHandler;
import wjl.cli.CommandHandlers;

import java.io.IOException;

public class MySshShellFactory implements ShellFactory {
    @Override
    public Command createShell(ChannelSession channel) throws IOException {
        CommandHandlers handlers = new CommandHandlers(
                new CommandHandler("System", new SystemView()));
        return new MySshShell(channel, handlers);
    }
}
