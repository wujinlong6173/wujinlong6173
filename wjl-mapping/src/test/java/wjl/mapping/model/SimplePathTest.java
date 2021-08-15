package wjl.mapping.model;

import org.junit.Assert;
import org.junit.Test;
import wjl.mapping.model.SimplePath;

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

    @Test
    public void testContain() {
        SimplePath p1 = new SimplePath("a", 0, "c");
        SimplePath p2 = new SimplePath("a", 0, "c");
        SimplePath p3 = new SimplePath("a", 1, "c");
        SimplePath p4 = new SimplePath("a", 1);

        Assert.assertTrue(p1.contain(p1));
        Assert.assertTrue(p1.contain(p2));
        Assert.assertTrue(p4.contain(p3));
        Assert.assertTrue(SimplePath.EMPTY.contain(p3));
        Assert.assertFalse(p1.contain(null));
        Assert.assertFalse(p3.contain(p2));
        Assert.assertFalse(p3.contain(p4));
        Assert.assertFalse(p3.contain(SimplePath.EMPTY));
    }
}