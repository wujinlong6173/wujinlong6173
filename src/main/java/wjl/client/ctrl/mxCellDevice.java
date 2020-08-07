package wjl.client.ctrl;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * 拓扑图中的设备元素
 */
public class mxCellDevice extends mxCell {
    private static final long serialVersionUID = 1L;

    static final int DEVICE_SIZE = 84;
    
    private static final String STYLE_UNDEPLOY = 
            "shape=image;image=/images/router-60.png;" +
            "verticalLabelPosition=bottom;verticalAlign=top;";
    
    /**
     * 
     * @param name 图标显示的名称
     * @param x 中心坐标
     * @param y 中心坐标
     */
    public mxCellDevice(String name, int x, int y) {
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
}
