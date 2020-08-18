package wjl.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 多级命令处理器，可以深入一级，也可以退出一级。
 */
public class CommandHandlers {
    private final static String QUIT = "quit";
    private final static String HELP = "help";

    private final Stack<CommandHandler> stack;

    public CommandHandlers(CommandHandler root) {
        stack = new Stack<>();
        stack.add(root);
    }

    /**
     *
     * @param fullCmd 完整的一行命令，包括命令名称和参数。
     * @return 需要显示的多行字符串。
     */
    public List<String> handle(String fullCmd) {
        switch (fullCmd) {
            case QUIT: {
                if (stack.size() > 1) {
                    stack.pop();
                }
                return null;
            }

            case HELP: {
                CommandHandler handler = stack.peek();
                return handler.listCommands();
            }

            default: {
                List<String> msg = new ArrayList<>();
                CommandHandler handler = stack.peek();
                Object nextHandler = handler.handle(fullCmd, msg);
                if (nextHandler != null) {
                    stack.push(new CommandHandler(nextHandler));
                }
                return msg;
            }
        }
    }
}
