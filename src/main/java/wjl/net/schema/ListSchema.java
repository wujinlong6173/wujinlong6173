package wjl.net.schema;

import java.util.List;

public class ListSchema extends DataSchema {
    private List<String> keys;
    private DataSchema entrySchema;

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public DataSchema getEntrySchema() {
        return entrySchema;
    }

    public void setEntrySchema(DataSchema entrySchema) {
        this.entrySchema = entrySchema;
    }
}
