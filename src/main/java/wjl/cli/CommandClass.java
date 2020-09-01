package wjl.cli;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个类中所有处理命令行的方法
 */
class CommandClass {
    // 缓存已经解析过的类
    private static final Map<Class<?>, CommandClass> CACHE = new ConcurrentHashMap<>();

    // 键值是命令名称，即命令的第一个单词
    private Map<String, List<CommandMethod>> cmdMappingMethods;

    public static CommandClass build(Class<?> cls) {
        CommandClass ret = CACHE.get(cls);
        if (ret != null) {
            return ret;
        }

        Map<String, List<CommandMethod>> mapping = new HashMap<>();
        for (Method method : cls.getMethods()) {
            CommandMethod cmdMethod = CommandMethod.build(method);
            if (cmdMethod != null) {
                mapping.computeIfAbsent(cmdMethod.getCmdName(), key -> new ArrayList<CommandMethod>())
                        .add(cmdMethod);
            }
        }

        if (!mapping.isEmpty()) {
            ret = new CommandClass();
            ret.cmdMappingMethods = mapping;
            CACHE.put(cls, ret);
        }

        return ret;
    }

    public List<String> listCommands() {
        List<String> cmdHelp = new ArrayList<>();
        for (List<CommandMethod> byName : cmdMappingMethods.values()) {
            for (CommandMethod eachMethod : byName) {
                cmdHelp.add(eachMethod.getCmdFormat());
            }
        }
        return cmdHelp;
    }

    public Method findMethod(String[] splitCmd) {
        List<CommandMethod> byName = cmdMappingMethods.get(splitCmd[0]);
        if (byName == null) {
            return null;
        }

        for (CommandMethod eachMethod : byName) {
            if (eachMethod.match(splitCmd)) {
                return eachMethod.getMethod();
            }
        }

        return null;
    }
}
