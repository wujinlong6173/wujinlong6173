package wjl.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest {
    @Test
    public void getTelnetClient() {
        Config.load();
        assertNotNull(Config.get("telnet.client"));
    }
}