package wjl.datamodel.schema;

import wjl.util.ErrorCollector;

import java.util.Map;

public class MapSchema extends DataSchema {
    private DataSchema entrySchema;

    public DataSchema getEntrySchema() {
        return entrySchema;
    }

    public void setEntrySchema(DataSchema entrySchema) {
        this.entrySchema = entrySchema;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = super.serialize();
        ret.put(SchemaKeywords.TYPE, SchemaKeywords.MAP);
        if (entrySchema != null) {
            ret.put(SchemaKeywords.ENTRY_SCHEMA, entrySchema.serialize());
        }
        return ret;
    }

    @Override
    public void validate(Object input, ErrorCollector error) {

    }
}
