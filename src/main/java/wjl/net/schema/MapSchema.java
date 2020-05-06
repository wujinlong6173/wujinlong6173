package wjl.net.schema;

public class MapSchema extends DataSchema {
    private DataSchema entrySchema;

    public DataSchema getEntrySchema() {
        return entrySchema;
    }

    public void setEntrySchema(DataSchema entrySchema) {
        this.entrySchema = entrySchema;
    }
}
