package wjl.mapping.parser;

import wjl.utils.ErrorCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringParser {
    public static String parseRequired(ErrorCollector error, String key, Map<?,?> parent) {
        Object value = parent.get(key);
        if (value instanceof String) {
            return (String)value;
        } else if (value == null) {
            error.reportError(key, "require string, can't be null.");
        } else {
            error.reportError(key, String.format(Locale.ENGLISH,
                "require string, but is %s.", value.getClass().getName()));
        }
        return null;
    }

    public static List<String> parseList(ErrorCollector error, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return Collections.singletonList((String)value);
        } else if (value instanceof List) {
            int idx = 0;
            List<?> lstValue = (List<?>)value;
            List<String> ret = new ArrayList<>(lstValue.size());
            for (Object item : lstValue) {
                if (item instanceof String) {
                    ret.add((String)item);
                } else if (item == null) {
                    error.reportError(idx, "require string, but is null.");
                } else {
                    error.reportError(idx, String.format(Locale.ENGLISH,
                        "require string, but is %s.", item.getClass().getName()));
                }
            }
            return ret;
        } else {
            error.reportError(String.format(Locale.ENGLISH,
                "require list of string, but is %s.", value.getClass().getName()));
            return null;
        }
    }
}
