package wjl.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlUtilTest {
    @Test
    public void parseYaml() throws IOException {
        Map<?,?> data = YamlUtil.str2map("{name: JDZ, age: 2000, desc: null}");
        Map<String,Object> expt = new HashMap<>();
        expt.put("name", "JDZ");
        expt.put("age", 2000);
        expt.put("desc", null);
        Assert.assertEquals(expt, data);
    }
}