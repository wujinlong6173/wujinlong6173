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
     * 如果处理器堆栈为空，应该退出回话
     *
     * @return 处理器堆栈为空
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     *
     * @param fullCmd 完整的一行命令，包括命令名称和参数。
     * @return 需要显示的多行字符串。
     */
    public List<String> handle(String fullCmd) {
        switch (fullCmd) {
            case QUIT: {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
                return null;
            }

            case HELP: {
                CommandHandler handler = stack.peek();
                return handler.getMethods().listCommands();
            }

            default: {
                List<String> msg = new ArrayList<>();
                CommandHandler handler = stack.peek();
                CommandHandler nextHandler = handler.handle(fullCmd, msg);
                if (nextHandler != null) {
                    stack.push(nextHandler);
                }
                return msg;
            }
        }
    }

    /**
     * 获取命令提示符
     *
     * @return
     */
    public String getPrompt() {
        StringBuilder sb = new StringBuilder();
        for (CommandHandler handler : stack) {
            sb.append(handler.getPrompt());
            sb.append(">");
        }
        return sb.toString();
    }
}
