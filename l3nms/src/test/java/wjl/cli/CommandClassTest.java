package wjl.cli;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CommandClassTest implements CommandView {
    @Override
    public String getPrompt() {
        return null;
    }

    @Command(command="mpls")
    public String enableMpls() {
        return "mpls";
    }

    @Command(command="mpls ldp")
    public String enableMplsLdp() {
        return "mpls ldp";
    }

    @Command(command="mpls lsr-id {id}")
    public String setLsrId(String id) {
        return id;
    }

    @Command(command="vlan from {start} to {end}")
    public String setVLan(String start, String end) {
        return start + ":" + end;
    }

    @Test
    public void testBuild() {
        CommandClass cmdClass = CommandClass.build(CommandClassTest.class);
        assertNotNull(cmdClass);

        List<String> cmdList = cmdClass.listCommands();
        assertEquals(4, cmdList.size());

        String[] cmd1 = new String[]{"mpls"};
        CommandMethod mpls = cmdClass.findMethod(cmd1);
        assertEquals("enableMpls", mpls.getMethod().getName());
        assertEquals("mpls", mpls.invoke(this, cmd1));

        String[] cmd2 = new String[]{"mpls", "ldp"};
        CommandMethod ldp = cmdClass.findMethod(cmd2);
        assertEquals("enableMplsLdp", ldp.getMethod().getName());
        assertEquals("mpls ldp", ldp.invoke(this, cmd2));

        String[] cmd3 = new String[]{"mpls", "lsr-id", "192.168.1.1"};
        CommandMethod lsr = cmdClass.findMethod(cmd3);
        assertEquals("setLsrId", lsr.getMethod().getName());
        assertEquals("192.168.1.1", lsr.invoke(this, cmd3));

        String[] cmd4 = new String[]{"vlan", "from", "100", "to", "150"};
        CommandMethod vlan = cmdClass.findMethod(cmd4);
        assertEquals("setVLan", vlan.getMethod().getName());
        assertEquals("100:150", vlan.invoke(this, cmd4));
    }
}