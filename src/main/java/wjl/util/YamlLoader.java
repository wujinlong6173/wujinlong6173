package wjl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

public class YamlLoader {
    private static Logger LOGGER = LoggerFactory.getLogger(YamlLoader.class);
    private static ObjectMapper YAML = new ObjectMapper(new YAMLFactory());

    public static <T> T yamlToObject(Class<T> cls, String data) {
        try {
            return YAML.readValue(data, cls);
        } catch (IOException e) {
            String msg = String.format(Locale.ENGLISH, "yaml not match to %s", cls.getName());
            LOGGER.error(msg, e);
            e.printStackTrace();
            return null;
        }
    }
}
