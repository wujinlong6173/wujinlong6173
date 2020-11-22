package wjl.datamodel.schema;

import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = super.serialize();
        ret.put(SchemaKeywords.TYPE, SchemaKeywords.LIST);
        if (keys != null) {
            if (keys.size() == 1) {
                ret.put(SchemaKeywords.KEY, keys.get(0));
            } else {
                ret.put(SchemaKeywords.KEY, keys);
            }
        }
        if (entrySchema != null) {
            ret.put(SchemaKeywords.ENTRY_SCHEMA, entrySchema.serialize());
        }
        return ret;
    }
}
