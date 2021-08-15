package wjl.datamodel;

import wjl.datamodel.schema.*;
import wjl.utils.ErrorCollector;
import wjl.utils.YamlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchemaParser extends ErrorCollector {
    public ObjectSchema parse(String name, String yaml) {
        Map<?, ?> mapSchema = null;
        try {
            mapSchema = YamlUtil.str2map(yaml);
        } catch (IOException err) {
            reportError(name, err.getMessage());
        }
        if (mapSchema == null) {
            reportError(name, "schema must be map.");
            return null;
        }

        pushLocator(name);
        ObjectSchema ret = parseObjectSchema(name, mapSchema);
        popLocator();
        return ret;
    }

    private ObjectSchema parseObjectSchema(String name, Map<?,?> objSchema) {
        ObjectSchema ret = new ObjectSchema();
        ret.setName(name);
        ret.setRequired(parseRequired(objSchema));
        ret.setDefaultValue(objSchema.get(SchemaKeywords.DEFAULT));
        Object props = objSchema.get(SchemaKeywords.PROPERTIES);
        if (props == null) {
            reportError(SchemaKeywords.PROPERTIES, "properties is required.");
        } else if (props instanceof Map) {
            Map<?,?> mapProps = (Map<?,?>)props;
            List<DataSchema> children = new ArrayList<>(mapProps.size());
            for (Map.Entry<?,?> entry : mapProps.entrySet()) {
                pushLocator(name);
                children.add(parseDataSchema((String)entry.getKey(), entry.getValue()));
                popLocator();
            }
            ret.setChildren(children);
        } else {
            reportError(SchemaKeywords.PROPERTIES, "properties must be map.");
        }
        return ret;
    }

    private DataSchema parseDataSchema(String name, Object raw) {
        if (raw instanceof String) {
            LeafSchema leaf = new LeafSchema();
            leaf.setName(name);
            leaf.setType((String)raw);
            return leaf;
        } else if (raw instanceof Map) {
            Map<?,?> mapDef = (Map<?,?>)raw;
            Object type = mapDef.get(SchemaKeywords.TYPE);
            if (type == null) {
                return parseObjectSchema(name, mapDef);
            } else if (SchemaKeywords.LIST.equals(type)) {
                return parseListSchema(name, mapDef);
            } else if (SchemaKeywords.MAP.equals(type)) {
                return parseMapSchema(name, mapDef);
            } else if (type instanceof String) {
                LeafSchema leaf = new LeafSchema();
                leaf.setName(name);
                leaf.setType((String)type);
                leaf.setRequired(parseRequired(mapDef));
                leaf.setDefaultValue(mapDef.get(SchemaKeywords.DEFAULT));
                return leaf;
            } else {
                reportError(SchemaKeywords.TYPE, "should be string.");
                return null;
            }
        } else {
            reportError("should be object or string.");
            return null;
        }
    }

    private ListSchema parseListSchema(String name,  Map<?,?> listSchema) {
        ListSchema lst = new ListSchema();
        lst.setName(name);
        lst.setRequired(parseRequired(listSchema));
        lst.setDefaultValue(listSchema.get(SchemaKeywords.DEFAULT));
        Object entrySchema = listSchema.get(SchemaKeywords.ENTRY_SCHEMA);
        if (entrySchema == null) {
            reportError(SchemaKeywords.ENTRY_SCHEMA, "entry_schema is required for list.");
        } else {
            pushLocator(SchemaKeywords.ENTRY_SCHEMA);
            lst.setEntrySchema(parseDataSchema(name, entrySchema));
            popLocator();
        }
        return lst;
    }

    private MapSchema parseMapSchema(String name, Map<?,?> mapSchema) {
        MapSchema map = new MapSchema();
        map.setName(name);
        map.setRequired(parseRequired(mapSchema));
        map.setDefaultValue(mapSchema.get(SchemaKeywords.DEFAULT));
        Object entrySchema = mapSchema.get(SchemaKeywords.ENTRY_SCHEMA);
        if (entrySchema == null) {
            reportError(SchemaKeywords.ENTRY_SCHEMA, "entry_schema is required for map.");
        } else {
            pushLocator(SchemaKeywords.ENTRY_SCHEMA);
            map.setEntrySchema(parseDataSchema(name, entrySchema));
            popLocator();
        }
        return map;
    }

    private boolean parseRequired(Map<?,?> rawMap) {
        Object value = rawMap.get(SchemaKeywords.REQUIRED);
        if (value == null) {
            return false;
        } else if (Boolean.TRUE.equals(value)) {
            return true;
        } else if (Boolean.FALSE.equals(value)) {
            return false;
        } else {
            reportError(SchemaKeywords.REQUIRED, "must be boolean.");
            return false;
        }
    }
}
