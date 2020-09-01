package wjl.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class CommandHandler {
    private static final String ERROR_UNKNOWN_COMMAND = "unknown command";
    private final Object handler;
    private final String cmdName;
    private final CommandClass cmdClass;

    public static CommandHandler build(String cmdName, Object handler) {
        CommandClass cmdClass = CommandClass.build(handler.getClass());
        if (cmdClass != null) {
            return new CommandHandler(cmdName, handler, cmdClass);
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
    public CommandHandler(String cmdName, Object handler, CommandClass cmdClass) {
        this.cmdName = cmdName;
        this.handler = handler;
        this.cmdClass = cmdClass;
    }

    public CommandClass getCmdClass() {
        return cmdClass;
    }

    /**
     *
     * @param fullCmd 完整的一行命令，包括命令名称和参数。
     * @param outputMsg 接收需要返回的形象。
     * @return 如果进入下一级命令，则返回下一级命令的处理器。
     */
    public CommandHandler handle(String fullCmd, List<String> outputMsg) {
        String[] splitCmd = fullCmd.split("[ \t]+");
        Method method = cmdClass.findMethod(splitCmd);
        if (method == null) {
            outputMsg.add(ERROR_UNKNOWN_COMMAND);
            return null;
        }

        try {
            Object[] args = new Object[splitCmd.length - 1];
            for (int i = 1; i < splitCmd.length; i++) {
                args[i - 1] = splitCmd[i];
            }

            Object ret = method.invoke(handler, args);
            if (ret == null) {

            } else if (ret instanceof String) {
                outputMsg.add((String)ret);
            } else if (ret instanceof List) {
                for (Object retItem : (List<?>)ret) {
                    outputMsg.add((String)retItem);
                }
            } else if (ret instanceof Map) {

            } else {
                CommandClass subMethods = CommandClass.build(ret.getClass());
                if (subMethods != null) {
                    return new CommandHandler(splitCmd[0], ret, subMethods);
                }
            }
            return null;
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
            outputMsg.add(e.getMessage());
            return null;
        }
    }

    /**
     * 获取命令提示符
     *
     * @return
     */
    public String getPrompt() {
        return cmdName;
    }
}
