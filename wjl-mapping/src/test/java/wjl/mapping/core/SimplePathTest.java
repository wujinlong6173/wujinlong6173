package wjl.mapping.core;

import org.junit.Assert;
import org.junit.Test;
import wjl.mapping.core.model.SimplePath;

public class SimplePathTest {
    @Test
    public void testToString() {
        Assert.assertEquals("${}",
                new SimplePath().toString());
        Assert.assertEquals("${a}",
                new SimplePath("a").toString());
        Assert.assertEquals("${a.b.c}",
                new SimplePath("a", "b", "c").toString());
        Assert.assertEquals("${a[0][1].c}",
                new SimplePath("a", 0, 1, "c").toString());
        Assert.assertEquals("${[\"ip.v6\"].prefix}",
                new SimplePath("ip.v6", "prefix").toString());
    }
}