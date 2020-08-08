package wjl.client.ctrl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CreatePortActionTest {
    @Test
    public void testCalcAngle() {
        assertEquals(2, CreatePortAction.position(1.0, 0.0));
        assertEquals(3, CreatePortAction.position(1.0, 1.0));
        assertEquals(4, CreatePortAction.position(0.0, 1.0));
        assertEquals(5, CreatePortAction.position(-1.0, 1.0));
        assertEquals(6, CreatePortAction.position(-1.0, 0.0));
        assertEquals(7, CreatePortAction.position(-1.0, -1.0));
        assertEquals(0, CreatePortAction.position(0.0, -1.0));
        assertEquals(1, CreatePortAction.position(1.0, -1.0));
    }
    
    @Test
    public void testSpecialAngle() {
        assertEquals(0, CreatePortAction.position(-1.0, -30.0));
    }
}
