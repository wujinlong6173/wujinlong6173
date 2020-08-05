package wjl.client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class ClientMain 
{
    public static JFrame createFrame(JMenuBar menuBar, JPanel pane)
    {
        JFrame frame = new JFrame();
        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.setSize(870, 640);
        return frame;
    }
    
    public static void main( String[] args )
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
        mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";

        JMenuBar menuBar = new JMenuBar();
        
        mxGraph graph = new CustomGraph();
        mxGraphComponent component = new mxGraphComponent(graph);
        NetworkTopoPanel topo = new NetworkTopoPanel(component);
        JFrame mainFrame = createFrame(menuBar, topo);
        mainFrame.setTitle("L3Network");
        mainFrame.setVisible(true);
    }
}
