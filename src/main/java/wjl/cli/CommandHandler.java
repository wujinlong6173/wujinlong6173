package wjl.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {
    private static final String ERROR_UNKNOWN_COMMAND = "unknown command";
    private final Map<String, CommandMethod> commands;

    /**
     * 注册根命令，扫描输入对象的所有公开方法，如果有@CommandView或@Command标记，
     * 则注册为根命令，执行命令时，相当于调用handler.funcName(...)。
     *
     * @param handler 处理命令的对象
     */
    public CommandHandler(Object handler) {
        this.commands = new HashMap<>();
        Class<?> cls = handler.getClass();
        for (Method method : cls.getMethods()) {

            if (method.isAnnotationPresent(Command.class)) {
                CommandMethod cm = new CommandMethod();
                cm.handler = handler;
                cm.method = method;
                cm.view = false;
                commands.put(method.getAnnotation(Command.class).command(), cm);
            } else if (method.isAnnotationPresent(CommandView.class)) {
                if (method.getReturnType() != Void.class) {
                    CommandMethod cm = new CommandMethod();
                    cm.handler = handler;
                    cm.method = method;
                    cm.view = true;
                    commands.put(method.getAnnotation(CommandView.class).command(), cm);
                }
            }
        }
    }

    static class CommandMethod {
        Object handler;
        Method method;
        boolean view;
    }

    public List<String> listCommands() {
        List<String> cmdHelp = new ArrayList<>();
        for (Map.Entry<String, CommandMethod> entry : commands.entrySet()) {
            cmdHelp.add(entry.getKey());
        }
        return cmdHelp;
    }

    /**
     *
     * @param fullCmd 完整的一行命令，包括命令名称和参数。
     * @param outputMsg 接收需要返回的形象。
     * @return 如果进入下一级命令，则返回下一级命令的处理器。
     */
    public Object handle(String fullCmd, List<String> outputMsg) {
        String[] splitCmd = fullCmd.split("[ \t]+");
        CommandMethod method = commands.get(splitCmd[0]);
        if (method == null) {
            outputMsg.add(ERROR_UNKNOWN_COMMAND);
            return null;
        }

        try {
            Object[] args = new Object[splitCmd.length - 1];
            for (int i = 1; i < splitCmd.length; i++) {
                args[i - 1] = splitCmd[i];
            }

            Object ret = method.method.invoke(method.handler, args);
            return method.view ? ret : null;
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
            outputMsg.add(e.getMessage());
            return null;
        }
    }
}
