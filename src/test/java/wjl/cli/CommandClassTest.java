package wjl.cli;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

public class CommandClassTest {
    @Command(command="mpls")
    public void enableMpls() {
    }

    @Command(command="mpls ldp")
    public void enableMplsLdp() {
    }

    @Command(command="mpls lsr-id {id}")
    public void setLsrId(String id) {
    }

    @Test
    public void testBuild() {
        CommandClass cmdClass = CommandClass.build(CommandClassTest.class);
        assertNotNull(cmdClass);

        List<String> cmdList = cmdClass.listCommands();
        assertEquals(3, cmdList.size());

        Method mpls = cmdClass.findMethod(new String[]{"mpls"});
        Method ldp = cmdClass.findMethod(new String[]{"mpls", "ldp"});
        Method lsr = cmdClass.findMethod(new String[]{"mpls", "lsr-id", "192.168.1.1"});
        assertEquals("enableMpls", mpls.getName());
        assertEquals("enableMplsLdp", ldp.getName());
        assertEquals("setLsrId", lsr.getName());
    }
}