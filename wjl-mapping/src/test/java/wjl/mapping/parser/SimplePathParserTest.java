package wjl.mapping.parser;

import org.junit.Assert;
import org.junit.Test;

import wjl.mapping.core.model.SimplePath;

public class SimplePathParserTest {
    private SimplePathParser parser = new SimplePathParser();

    @Test
    public void parseDottedPath() {
        Assert.assertSame(SimplePath.EMPTY, parser.parse("${}"));
        Assert.assertEquals(new SimplePath("a", "b", "c"), parser.parse("${a.b.c}"));
        Assert.assertEquals(new SimplePath("a-b_c"), parser.parse("${a-b_c}"));
        Assert.assertEquals(new SimplePath(123), parser.parse("${123}"));
        Assert.assertEquals(new SimplePath("a", 90, "b"), parser.parse("${a.90.b}"));

        Assert.assertNull(parser.parse("${abc*.xyz}"));
        Assert.assertEquals("unsupported char '*' at 3 in 'abc*.xyz'", parser.getError());

        Assert.assertNull(parser.parse("${.}"));
        Assert.assertEquals("unsupported char '.' at 0 in '.'", parser.getError());

        //Assert.assertNull(parser.parse("${abc.}"));
        //Assert.assertEquals("unsupported char '.' at 3 in 'abc.'", parser.getError());

        Assert.assertNull(parser.parse("${abc. xyz}"));
        Assert.assertEquals("unsupported char ' ' at 4 in 'abc. xyz'", parser.getError());
    }
}