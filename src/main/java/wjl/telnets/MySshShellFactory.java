package wjl.telnets;

import com.huawei.cli.HuaWeiSystem;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import wjl.cli.CommandHandler;
import wjl.cli.CommandHandlers;
import wjl.cli.CommandView;
import wjl.cli.CommandViewFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 至少要添加一个CommandViewFactory，否则没有意义。
 */
public class MySshShellFactory implements ShellFactory {
    private final List<CommandViewFactory> viewFactoryList = new ArrayList<>();

    public void addCommandViewFactory(CommandViewFactory factory) {
        viewFactoryList.add(factory);
    }

    @Override
    public Command createShell(ChannelSession channel) throws IOException {
        final String user = channel.getSession().getUsername();
        for (CommandViewFactory factory : viewFactoryList) {
            CommandView cmdView = factory.build(user);
            CommandHandlers handlers = new CommandHandlers(
                    CommandHandler.build(cmdView));
            return new MySshShell(channel, handlers);
        }
        return null;
    }
}
