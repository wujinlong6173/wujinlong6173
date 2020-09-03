package wjl.telnets;

import com.huawei.cli.HuaWeiSystem;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import wjl.cli.CommandHandler;
import wjl.cli.CommandHandlers;
import wjl.cli.CommandView;

import java.io.IOException;

public class MySshShellFactory implements ShellFactory {
    @Override
    public Command createShell(ChannelSession channel) throws IOException {
        final String user = channel.getSession().getUsername();
        CommandView cmdView = HuaWeiSystem.getCommandView(user);
        CommandHandlers handlers = new CommandHandlers(
                CommandHandler.build(cmdView));
        return new MySshShell(channel, handlers);
    }
}
