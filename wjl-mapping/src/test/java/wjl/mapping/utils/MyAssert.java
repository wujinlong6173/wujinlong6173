package wjl.mapping.utils;

import org.junit.Assert;

import java.util.Objects;

public class MyAssert {
    public static void assertEquals(Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            return;
        }
        Assert.assertEquals(YamlLoader.objToStr(expected), YamlLoader.objToStr(actual));
    }
}
