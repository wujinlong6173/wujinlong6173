package wjl.util;

import java.util.HashMap;
import java.util.Map;

public class ErrorCollector {
    private final int MAX_LEVEL = 100;
    private final Object [] location = new Object[MAX_LEVEL];
    private int stackTop = -1;
    private final Map<String, String> errors = new HashMap<>();

    public void pushLocator(String loc) {
        stackTop++;
        if (stackTop < MAX_LEVEL) {
            location[stackTop] = loc;
        }
    }

    public void pushLocator(int loc) {
        stackTop++;
        if (stackTop < MAX_LEVEL) {
            location[stackTop] = loc;
        }
    }

    public void popLocator() {
        if (stackTop > 0) {
            stackTop--;
        }
    }

    public void reportError(String msg) {
        errors.put(buildLocation(), msg);
    }

    public void reportError(String loc, String msg) {
        pushLocator(loc);
        errors.put(buildLocation(), msg);
        popLocator();
    }

    public Map<String, String> getErrors() {
        return errors.isEmpty() ? null : errors;
    }

    private String buildLocation() {
        StringBuilder sb = new StringBuilder();
        int top = Math.min(stackTop, MAX_LEVEL - 1);
        for (int i = 0; i <= top; i++) {
            if (location[i] instanceof String) {
                sb.append('.').append(location[i]);
            } else {
                sb.append('[').append(location[i]).append(']');
            }
        }
        return sb.toString();
    }
}
