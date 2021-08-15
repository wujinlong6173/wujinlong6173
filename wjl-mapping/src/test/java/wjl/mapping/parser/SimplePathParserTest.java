package wjl.mapping.parser;

import org.junit.Assert;
import org.junit.Test;

import wjl.mapping.model.SimplePath;

public class SimplePathParserTest {
    private SimplePathParser parser = new SimplePathParser();

    @Test
    public void parsePathWithDot() {
        Assert.assertSame(SimplePath.EMPTY, parser.parse("${}"));
        Assert.assertEquals(new SimplePath("a", "b", "c"), parser.parse("${a.b.c}"));
        Assert.assertEquals(new SimplePath("a-b_c"), parser.parse("${a-b_c}"));
        Assert.assertEquals(new SimplePath(123), parser.parse("${123}"));
        Assert.assertEquals(new SimplePath("a", 90, "b"), parser.parse("${a.90.b}"));

        Assert.assertNull(parser.parse("${?}"));
        Assert.assertEquals("unsupported char '?' at 0 in '?'", parser.getError());

        Assert.assertNull(parser.parse("${abc*.xyz}"));
        Assert.assertEquals("unsupported char '*' at 3 in 'abc*.xyz'", parser.getError());

        Assert.assertNull(parser.parse("${.}"));
        Assert.assertEquals("unsupported char '.' at 0 in '.'", parser.getError());

        Assert.assertNull(parser.parse("${abc.}"));
        Assert.assertEquals("unsupported char '.' at 3 in 'abc.'", parser.getError());

        Assert.assertNull(parser.parse("${abc. xyz}"));
        Assert.assertEquals("unsupported char ' ' at 4 in 'abc. xyz'", parser.getError());
    }

    @Test
    public void parsePathWithSquareBrackets() {
        Assert.assertEquals(new SimplePath(11, 12, "abc"), parser.parse("${[11][12][abc]}"));
        Assert.assertEquals(new SimplePath("items", 10, "name"), parser.parse("${items[10].name}"));

        Assert.assertNull(parser.parse("${[]}"));
        Assert.assertEquals("empty '[]' at 1 in '[]'", parser.getError());

        Assert.assertNull(parser.parse("${[}"));
        Assert.assertEquals("require ']' at 1 in '['", parser.getError());

        Assert.assertNull(parser.parse("${[12}"));
        Assert.assertEquals("require ']' at 3 in '[12'", parser.getError());

        Assert.assertNull(parser.parse("${[0]abc}"));
        Assert.assertEquals("unsupported char 'a' at 3 in '[0]abc'", parser.getError());

        Assert.assertNull(parser.parse("${[[0]]}"));
        Assert.assertEquals("unsupported char '[' at 1 in '[[0]]'", parser.getError());

        Assert.assertNull(parser.parse("${[0] [1]}"));
        Assert.assertEquals("unsupported char ' ' at 3 in '[0] [1]'", parser.getError());
    }
}