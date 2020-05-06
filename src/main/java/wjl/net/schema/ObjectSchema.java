package wjl.net.schema;

import java.util.List;

public class ObjectSchema extends DataSchema {
    // 保留属性定义的顺序
    private List<DataSchema> children;

    public List<DataSchema> getChildren() {
        return children;
    }

    public void setChildren(List<DataSchema> children) {
        this.children = children;
    }
}
