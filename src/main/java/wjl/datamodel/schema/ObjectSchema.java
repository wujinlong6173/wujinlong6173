package wjl.datamodel.schema;

import wjl.util.ErrorCollector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectSchema extends DataSchema {
    // 保留属性定义的顺序
    private List<DataSchema> children;

    public List<DataSchema> getChildren() {
        return children;
    }

    public void setChildren(List<DataSchema> children) {
        this.children = children;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = super.serialize();
        if (children != null) {
            Map<String, Object> properties = new LinkedHashMap<>();
            for (DataSchema child : children) {
                properties.put(child.getName(), child.serialize());
            }
            ret.put(SchemaKeywords.PROPERTIES, properties);
        }
        return ret;
    }

    @Override
    public void validate(Object input, ErrorCollector error) {
        if (input == null) {
            if (required) {
                error.reportError(name, "require object.");
            }
        } else if (input instanceof Map) {
            error.pushLocator(name);
            Map<?,?> mapInput = (Map<?,?>)input;
            for (DataSchema attrSchema : children) {
                Object attrValue = mapInput.get(attrSchema.name);
                attrSchema.validate(attrValue, error);
            }
            error.popLocator();
        } else if (children != null) {
            error.reportError(name, String.format("require object, not %s.", input.getClass().getName()));
        }
    }
}
