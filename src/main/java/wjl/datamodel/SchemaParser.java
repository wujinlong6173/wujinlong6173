package wjl.datamodel;

import wjl.datamodel.schema.*;
import wjl.util.ErrorCollector;
import wjl.util.YamlLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchemaParser extends ErrorCollector {
    private static final String ENTRY_SCHEMA = "entry_schema";
    private static final String PROPERTIES = "properties";
    private static final String TYPE = "type";
    private static final String TYPE_LIST = "list";
    private static final String TYPE_MAP = "map";

    public ObjectSchema parse(String name, String yaml) {
        Map<?,?> mapSchema = YamlLoader.yamlToObject(Map.class, yaml);
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
        Object props = objSchema.get(PROPERTIES);
        if (props == null) {
            reportError(PROPERTIES, "properties is required.");
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
            reportError(PROPERTIES, "properties must be map.");
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
            Object type = mapDef.get(TYPE);
            if (type == null) {
                return parseObjectSchema(name, mapDef);
            } else if (TYPE_LIST.equals(type)) {
                return parseListSchema(name, mapDef);
            } else if (TYPE_MAP.equals(type)) {
                return parseMapSchema(name, mapDef);
            } else if (type instanceof String) {
                LeafSchema leaf = new LeafSchema();
                leaf.setName(name);
                leaf.setType((String)type);
                return leaf;
            } else {
                reportError(TYPE, "should be string.");
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
        Object entrySchema = listSchema.get(ENTRY_SCHEMA);
        if (entrySchema == null) {
            reportError(ENTRY_SCHEMA, "entry_schema is required for list.");
        } else {
            pushLocator(ENTRY_SCHEMA);
            lst.setEntrySchema(parseDataSchema(name, entrySchema));
            popLocator();
        }
        return lst;
    }

    private MapSchema parseMapSchema(String name, Map<?,?> mapSchema) {
        MapSchema map = new MapSchema();
        map.setName(name);
        Object entrySchema = mapSchema.get(ENTRY_SCHEMA);
        if (entrySchema == null) {
            reportError(ENTRY_SCHEMA, "entry_schema is required for map.");
        } else {
            pushLocator(ENTRY_SCHEMA);
            map.setEntrySchema(parseDataSchema(name, entrySchema));
            popLocator();
        }
        return map;
    }
}
