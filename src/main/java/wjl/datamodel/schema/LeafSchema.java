package wjl.datamodel.schema;

import wjl.util.ErrorCollector;

import java.util.HashMap;
import java.util.Map;

public class LeafSchema extends DataSchema {
    private static final Map<String, Class<?>> TYPE_CLASS;

    static {
        TYPE_CLASS = new HashMap<>();
        TYPE_CLASS.put("string", String.class);
        TYPE_CLASS.put("integer", Integer.class);
        TYPE_CLASS.put("boolean", Boolean.class);
        TYPE_CLASS.put("double", Double.class);
    }

    // 基本数据类型
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = super.serialize();
        ret.put(SchemaKeywords.TYPE, type);
        return ret;
    }

    @Override
    public void validate(Object input, ErrorCollector error) {
        if (input == null) {
            error.reportError(name, String.format("require %s, but null.", type));
        } else {
            Class<?> cls = TYPE_CLASS.get(type);
            if (cls != null && !(cls.isInstance(input))) {
                error.reportError(name, String.format("require %s, but is %s.", type, input.getClass().getName()));
            }
        }
    }
}
