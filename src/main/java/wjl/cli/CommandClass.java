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
public class CommandClass {
    private static final Map<Class<?>, CommandClass> CACHE = new ConcurrentHashMap<>();
    private Map<String, Method> cmdMappingMethods;

    public static CommandClass build(Class<?> cls) {
        CommandClass ret = CACHE.get(cls);
        if (ret != null) {
            return ret;
        }

        Map<String, Method> mapping = new HashMap<>();
        for (Method method : cls.getMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                mapping.put(method.getAnnotation(Command.class).command(), method);
            }
        }

        if (!mapping.isEmpty()) {
            ret = new CommandClass();
            ret.cmdMappingMethods = mapping;
        }
        return ret;
    }

    public List<String> listCommands() {
        List<String> cmdHelp = new ArrayList<>();
        for (Map.Entry<String, Method> entry : cmdMappingMethods.entrySet()) {
            cmdHelp.add(entry.getKey());
        }
        return cmdHelp;
    }

    public Method findMethod(String cmd) {
        return cmdMappingMethods.get(cmd);
    }
}
