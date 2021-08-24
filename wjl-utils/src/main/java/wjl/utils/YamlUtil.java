package wjl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 操作YAML数据的工具类。
 *
 * @author wujinlong
 * @since 2021-8-14
 */
public final class YamlUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtil.class);
    private static final ObjectMapper YAML = new ObjectMapper(new YAMLFactory());
    private static final TypeReference<Map<String, Object>> MAP_STR_OBJ = new TypeReference<Map<String, Object>>(){};

    public static Object str2obj(String yamlStr) throws IOException {
        if (yamlStr == null || yamlStr.isEmpty()) {
            return null;
        } else {
            return YAML.readValue(yamlStr, Object.class);
        }
    }

    public static <T> T str2obj(String yamlStr, Class<T> objType) throws IOException {
        if (yamlStr == null || yamlStr.isEmpty()) {
            return null;
        } else {
            return YAML.readValue(yamlStr, objType);
        }
    }

    public static Map<String, Object> str2map(String yamlStr) throws IOException {
        if (yamlStr == null || yamlStr.isEmpty()) {
            return null;
        } else {
            return YAML.readValue(yamlStr, MAP_STR_OBJ);
        }
    }

    public static String obj2str(Object obj) throws JsonProcessingException {
        return YAML.writeValueAsString(obj);
    }

    public static Object file2obj(String filename) throws IOException {
        return YAML.readValue(new File(filename), Object.class);
    }

    public static <T> T file2obj( Class<T> cls, String filename) {
        try {
            return YAML.readValue(new File(filename), cls);
        } catch (IOException err) {
            LOGGER.error("read yaml file {} to class {} failed.", filename, cls.getName());
            return null;
        }
    }

    public static boolean obj2file(String filename, Object data) {
        try {
            YAML.writeValue(new File(filename), data);
            return true;
        } catch (IOException e) {

            LOGGER.error("write object {} to yaml file {} failed.",
                data != null ? data.getClass().getName() : null, filename);
            return false;
        }
    }

    public static <T> T convert(Object obj, Class<T> objType) {
        return YAML.convertValue(obj, objType);
    }
}
