package com.huawei.schema;

import org.junit.Test;
import wjl.datamodel.SchemaParser;
import wjl.datamodel.schema.LeafSchema;
import wjl.datamodel.schema.ListSchema;
import wjl.datamodel.schema.MapSchema;
import wjl.datamodel.schema.ObjectSchema;

import static org.junit.Assert.*;

public class SchemaParserTest {
    private SchemaParser parser = new SchemaParser();

    @Test
    public void parseLeaf() {
        ObjectSchema obj = parser.parse("VRF", "properties: {name: string, host: {type: string}}");
        assertNull(parser.getErrors());
        assertEquals("VRF", obj.getName());
        assertEquals(2, obj.getChildren().size());
        LeafSchema name = (LeafSchema)obj.getChildren().get(0);
        assertEquals("name", name.getName());
        assertEquals("string", name.getType());
        LeafSchema host = (LeafSchema)obj.getChildren().get(1);
        assertEquals("host", host.getName());
        assertEquals("string", host.getType());
    }

    @Test
    public void parseList() {
        ObjectSchema obj = parser.parse("VRF", "properties: {tags: {type: list, entry_schema: string}}");
        assertNull(parser.getErrors());
        ListSchema tags = (ListSchema)obj.getChildren().get(0);
        assertEquals("tags", tags.getName());
        LeafSchema entry = (LeafSchema)tags.getEntrySchema();
        assertEquals("tags", entry.getName());
        assertEquals("string", entry.getType());
    }

    @Test
    public void parseMap() {
        ObjectSchema obj = parser.parse("VRF", "properties: {tags: {type: map, entry_schema: string}}");
        assertNull(parser.getErrors());
        MapSchema tags = (MapSchema)obj.getChildren().get(0);
        assertEquals("tags", tags.getName());
        LeafSchema entry = (LeafSchema)tags.getEntrySchema();
        assertEquals("tags", entry.getName());
        assertEquals("string", entry.getType());
    }
}