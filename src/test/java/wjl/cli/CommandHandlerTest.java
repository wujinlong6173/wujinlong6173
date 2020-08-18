package wjl.cli;

import org.junit.Test;

public class CommandHandlerTest {
    @Test
    public void split() {
        String cmd = "ip address";
        String [] ret = cmd.split("[ \t]+");
        for (String each : ret) {
            System.out.println(each);
        }
    }
}