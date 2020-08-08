package wjl.client.ctrl;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

/**
 * 拓扑图中的链路元素
 */
public class mxCellLink extends mxCell {
    private static final long serialVersionUID = 1L;
    
    private static final String STYLE_LINK = "align=center;" +
            "verticalAlign=middle;labelBackgroundColor=white;" + 
            "startArrow=none;endArrow=none;dashed=true";

    private static final String STYLE_DEPLOY = "align=center;" +
            "verticalAlign=middle;labelBackgroundColor=white;" + 
            "startArrow=none;endArrow=none;";
    
    public mxCellLink(String name, mxCellPort src, mxCellPort dst) {
        super(name, new mxGeometry(), STYLE_LINK);
        setSource(src);
        setTarget(dst);
        setEdge(true);
    }

    public void changeDeployState(boolean deploy) {
        setStyle(deploy ? STYLE_DEPLOY : STYLE_LINK);        
    }
}
