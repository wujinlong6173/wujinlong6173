package wjl.datamodel;

import org.junit.Test;
import wjl.datamodel.schema.ObjectSchema;
import wjl.utils.YamlUtil;

import java.io.IOException;

import static org.junit.Assert.*;

public class SchemaParserTest {
    @Test
    public void testParse() throws IOException {
        String raw = "properties:\n" +
                "  name: {type: string, required: true}\n" +
                "  desc: {type: string}\n" +
                "  tpList:\n" +
                "    type: list\n" +
                "    entry_schema:\n" +
                "      properties:\n" +
                "        id: {type: string}\n" +
                "        ip: {type: string, default: 127.0.0.1}\n" +
                "  additionInfo:\n" +
                "    type: map\n" +
                "    entry_schema: {type: string}\n";

        SchemaParser parser = new SchemaParser();
        ObjectSchema schema = parser.parse("test", raw);
        assertNull(parser.getErrors());
        assertEquals(YamlUtil.str2obj(raw), schema.serialize());
    }
}