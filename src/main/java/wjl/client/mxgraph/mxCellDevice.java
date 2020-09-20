package wjl.client.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;

/**
 * 拓扑图中的设备元素
 */
public class mxCellDevice extends mxCell {
    private static final long serialVersionUID = 1L;

    static final int DEVICE_SIZE = 84;
    
    private static final String STYLE_UNDEPLOY = 
            "shape=image;image=/images/router-60.png;" +
            "verticalLabelPosition=bottom;verticalAlign=top;";
    
    private static final String STYLE_DEPLOY = 
            "shape=image;image=/images/router-deploy.png;" +
            "verticalLabelPosition=bottom;verticalAlign=top;";

    // 界面显示的限制，一个设备最多支持八个端口
    private static final int MAX_PORT = 8;
    private static final double POSITION[][];
    private static final int XY_SHIFT = mxCellDevice.DEVICE_SIZE / 2;

    static {
        int r = (mxCellDevice.DEVICE_SIZE - mxCellPort.PORT_SIZE) / 2;
        double r2 = r * Math.sqrt(0.5);
        POSITION = new double[MAX_PORT][];
        POSITION[0] = new double[] {XY_SHIFT,    XY_SHIFT-r};
        POSITION[1] = new double[] {XY_SHIFT+r2, XY_SHIFT-r2};
        POSITION[2] = new double[] {XY_SHIFT+r,  XY_SHIFT};
        POSITION[3] = new double[] {XY_SHIFT+r2, XY_SHIFT+r2};
        POSITION[4] = new double[] {XY_SHIFT,    XY_SHIFT+r};
        POSITION[5] = new double[] {XY_SHIFT-r2, XY_SHIFT+r2};
        POSITION[6] = new double[] {XY_SHIFT-r,  XY_SHIFT};
        POSITION[7] = new double[] {XY_SHIFT-r2, XY_SHIFT-r2};
    }

    /**
     * 
     * @param name 图标显示的名称
     * @param x 中心坐标
     * @param y 中心坐标
     */
    public mxCellDevice(String name, double x, double y) {
        super(name,
            new mxGeometry(x - DEVICE_SIZE / 2, y - DEVICE_SIZE / 2, DEVICE_SIZE, DEVICE_SIZE),
            STYLE_UNDEPLOY);
        setVertex(true);
        setConnectable(false);
    }
    
    public double getCenterX() {
        return geometry.getCenterX();
    }
    
    public double getCenterY() {
        return geometry.getCenterY();
    }

    public void changeDeployState(boolean deploy) {
        setStyle(deploy ? STYLE_DEPLOY : STYLE_UNDEPLOY);
    }

    public int emptyPosition(int start) {
        if (getChildCount() >= MAX_PORT) {
            return -1;
        }

        int idx;
        boolean[] flags = new boolean[MAX_PORT];
        for (idx = 0; idx < getChildCount(); idx++) {
            mxICell child = getChildAt(idx);
            int p = position(child.getGeometry().getCenterX() - XY_SHIFT,
                    child.getGeometry().getCenterY() - XY_SHIFT);
            flags[p] = true;
        }

        for (idx = 0; idx < MAX_PORT; idx++) {
            if (!flags[(start + idx) % MAX_PORT]) {
                return (start + idx) % MAX_PORT;
            }
        }
        return -1;
    }

    // 从顶部算起，顺时针顺序，依次编号为0~7
    public static int position(double dx, double dy) {
        double angle = Math.atan(dy / dx);
        angle += Math.PI * 5 / 8;
        int dir = (int)Math.floor(angle * 4.0 / Math.PI);
        if (dx < 0) {
            dir = (dir + 4) % 8;
        }
        return dir;
    }

    public static double getPositionX(int dir) {
        return POSITION[dir][0];
    }

    public static double getPositionY(int dir) {
        return POSITION[dir][1];
    }
}
