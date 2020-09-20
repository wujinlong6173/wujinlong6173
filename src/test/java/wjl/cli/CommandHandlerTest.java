package wjl.cli;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommandHandlerTest {
    private String[] split(String cmd) {
        return cmd.split("[ \t]+");
    }

    @Test
    public void testMatch() {
        CommandMethod m1 = new CommandMethod("peer group {name} hub", null);
        assertTrue(m1.match(split("peer group g1 hub")));
        assertFalse(m1.match(split("peer group g1 spoke")));
    }
}