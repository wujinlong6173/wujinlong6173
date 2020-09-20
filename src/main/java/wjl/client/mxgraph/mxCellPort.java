package wjl.client.mxgraph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * 拓扑图中的端口元素
 */
public class mxCellPort extends mxCell {
    private static final long serialVersionUID = 1L;

    static final int PORT_SIZE = 16;
    
    private static final String STYLE_PORT = 
            "shape=image;image=/images/5-pin-connector-32.png;" +
            "labelPosition=middle;verticalAlign=top;" +
            "movable=false;"; 

    /**
     * 
     * @param name 图标显示的名称
     * @param x 中心坐标
     * @param y 中心坐标
     */
    public mxCellPort(String name, double x, double y) {
        super(name,
            new mxGeometry(x - PORT_SIZE / 2, y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE),
            STYLE_PORT);
        setVertex(true);
    }
}
