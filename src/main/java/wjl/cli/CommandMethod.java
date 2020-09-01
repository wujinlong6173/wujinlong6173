package wjl.cli;

import java.lang.reflect.Method;

/**
 * 处理一条命令的方法
 */
class CommandMethod {
    // 完整的命令格式，例如ip address {ip} {mask}
    private final String cmdFormat;

    // 处理该命令的方法
    private final Method method;

    // 命令名称，即命令的第一个单词
    private final String[] splitCmdFormat;

    public static CommandMethod build(Method method) {
        if (method.isAnnotationPresent(Command.class)) {
            Command annotation = method.getAnnotation(Command.class);
            return new CommandMethod(annotation.command(), method);
        } else {
            return null;
        }
    }

    public CommandMethod(String cmdFormat, Method method) {
        this.cmdFormat = cmdFormat;
        this.method = method;

        this.splitCmdFormat = cmdFormat.split("[ \t]+");
    }

    public boolean match(String[] splitCmd) {
        return splitCmd.length == splitCmdFormat.length;
    }

    public String getCmdFormat() {
        return cmdFormat;
    }

    public Method getMethod() {
        return method;
    }

    public String getCmdName() {
        return splitCmdFormat[0];
    }
}
