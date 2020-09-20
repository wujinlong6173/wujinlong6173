package wjl.cli;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
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
    private final int[] cmdParamsPosition;

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

        int paramsCount = 0;
        for (String item : this.splitCmdFormat) {
            if (item.matches("\\{[_0-9A-Za-z]+}")) {
                paramsCount ++;
            }
        }
        this.cmdParamsPosition = new int[paramsCount];
        paramsCount = 0;
        for (int position = 0; position < splitCmdFormat.length; position++) {
            if (splitCmdFormat[position].matches("\\{[_0-9A-Za-z]+}")) {
                cmdParamsPosition[paramsCount++] = position;
            }
        }
    }

    public boolean match(String[] splitCmd) {
        if (splitCmd.length != splitCmdFormat.length) {
            return false;
        }

        for (int i = 0; i < splitCmdFormat.length; i++) {
            if (splitCmdFormat[i].startsWith("{")) {
                continue;
            }
            if (!StringUtils.equals(splitCmdFormat[i], splitCmd[i])) {
                return false;
            }
        }
        return true;
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

    /**
     * 处理输入的命令
     *
     * @param handler 当前的命令视图
     * @param splitCmd 输入的命令
     * @return 执行结果或错误信息
     */
    public Object invoke(CommandView handler, String[] splitCmd) {
        try {
            Object[] args = new Object[cmdParamsPosition.length];
            for (int i = 0; i < cmdParamsPosition.length; i++) {
                args[i] = splitCmd[cmdParamsPosition[i]];
            }
            return method.invoke(handler, args);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
