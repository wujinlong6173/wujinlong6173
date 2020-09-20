package wjl.client.mxgraph;

import org.junit.Test;

import static org.junit.Assert.*;

public class mxCellDeviceTest {
    @Test
    public void testCalcAngle() {
        assertEquals(2, mxCellDevice.position(1.0, 0.0));
        assertEquals(3, mxCellDevice.position(1.0, 1.0));
        assertEquals(4, mxCellDevice.position(0.0, 1.0));
        assertEquals(5, mxCellDevice.position(-1.0, 1.0));
        assertEquals(6, mxCellDevice.position(-1.0, 0.0));
        assertEquals(7, mxCellDevice.position(-1.0, -1.0));
        assertEquals(0, mxCellDevice.position(0.0, -1.0));
        assertEquals(1, mxCellDevice.position(1.0, -1.0));
    }

    @Test
    public void testSpecialAngle() {
        assertEquals(0, mxCellDevice.position(-1.0, -30.0));
    }
}