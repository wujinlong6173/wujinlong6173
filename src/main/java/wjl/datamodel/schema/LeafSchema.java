package wjl.datamodel.schema;

import java.util.Map;

public class LeafSchema extends DataSchema {
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
}
