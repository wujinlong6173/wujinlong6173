package wjl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties cfg = new Properties();

    public static void load() {
        try (InputStream isCfg = new FileInputStream(
                new File("/client/cfg.properties"))) {
            cfg.load(isCfg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String name) {
        return (String)cfg.get(name);
    }
}
