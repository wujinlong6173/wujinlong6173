package wjl.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class YamlLoaderTest {
    @Test
    public void parseYaml() {
        Map<?,?> data = YamlLoader.yamlToObject(Map.class, "{name: JDZ, age: 2000, desc: null}");
        Map<String,Object> expt = new HashMap<>();
        expt.put("name", "JDZ");
        expt.put("age", 2000);
        expt.put("desc", null);
        assertEquals(expt, data);
    }
}