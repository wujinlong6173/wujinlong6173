package wjl.mapping.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.junit.Assert;

public class YamlLoader {
    private static ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    
    public static String objToStr(Object obj) {
        try {
            return YAML.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }
}
