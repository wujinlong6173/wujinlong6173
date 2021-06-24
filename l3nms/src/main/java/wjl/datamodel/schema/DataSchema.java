package wjl.datamodel.schema;

import wjl.util.ErrorCollector;

import java.util.HashMap;
import java.util.Map;

public abstract class DataSchema {
    protected boolean required;
    protected String name;
    protected Object defaultValue;
    // 按位使用的标记
    protected int flag;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        if (required) {
            ret.put(SchemaKeywords.REQUIRED, Boolean.TRUE);
        }
        if (defaultValue != null) {
            ret.put(SchemaKeywords.DEFAULT, defaultValue);
        }
        return ret;
    }

    public abstract void validate(Object input, ErrorCollector error);
}
