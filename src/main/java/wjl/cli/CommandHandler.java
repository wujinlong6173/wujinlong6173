package wjl.cli;

import java.util.List;

public class CommandHandler {
    private static final String ERROR_UNKNOWN_COMMAND = "unknown command";
    private final CommandView handler;
    private final CommandClass cmdClass;

    public static CommandHandler build(CommandView handler) {
        CommandClass cmdClass = CommandClass.build(handler.getClass());
        if (cmdClass != null) {
            return new CommandHandler(handler, cmdClass);
        } else {
            return null;
        }
    }

    /**
     * 注册根命令，扫描输入对象的所有公开方法，如果有@CommandView或@Command标记，
     * 则注册为根命令，执行命令时，相当于调用handler.funcName(...)。
     *
     * @param handler 处理命令的对象
     */
    public CommandHandler(CommandView handler, CommandClass cmdClass) {
        this.handler = handler;
        this.cmdClass = cmdClass;
    }

    public CommandClass getCmdClass() {
        return cmdClass;
    }

    public CommandView getHandler() {
        return handler;
    }

    /**
     *
     * @param fullCmd 完整的一行命令，包括命令名称和参数。
     * @param outputMsg 接收需要返回的形象。
     * @return 如果进入下一级命令，则返回下一级命令的处理器。
     */
    public CommandHandler handle(String fullCmd, List<String> outputMsg) {
        String[] splitCmd = fullCmd.split("[ \t]+");
        CommandMethod method = cmdClass.findMethod(splitCmd);
        if (method == null) {
            outputMsg.add(ERROR_UNKNOWN_COMMAND);
            return null;
        }

        Object ret = method.invoke(handler, splitCmd);
        if (ret == null) {

        } else if (ret instanceof String) {
            outputMsg.add((String)ret);
        } else if (ret instanceof List) {
            for (Object retItem : (List<?>)ret) {
                outputMsg.add((String)retItem);
            }
        } else if (ret instanceof CommandView) {
            CommandView cmdView = (CommandView)ret;
            CommandClass subClass = CommandClass.build(cmdView.getClass());
            if (subClass != null) {
                return new CommandHandler(cmdView, subClass);
            }
        }
        return null;
    }
}
