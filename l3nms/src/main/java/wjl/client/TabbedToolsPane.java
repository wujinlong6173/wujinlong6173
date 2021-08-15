package wjl.client;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.mxgraph.model.mxCell;
import wjl.utils.YamlUtil;

/**
 * 支持往拓扑图拖拽的工具栏
 */
public class TabbedToolsPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;

    public void add(String tabName, String cfgFileName) {
        initEditorPalette(insertPalette(tabName), cfgFileName); 
    }

    private EditorPalette insertPalette(String title) {
        final EditorPalette palette = new EditorPalette();
        final JScrollPane scrollPane = new JScrollPane(palette);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(title, scrollPane);

        // 跟随父面板改变工具盘的界面大小
        this.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                int w = scrollPane.getWidth()
                        - scrollPane.getVerticalScrollBar().getWidth();
                palette.setPreferredWidth(w);
            }

        });

        return palette;
    }
    
    private void initEditorPalette(EditorPalette palette, String filename) {
        EditorPaletteConf conf = YamlUtil.file2obj(filename, EditorPaletteConf.class);
        if (conf == null) {
            return;
        }
        
        if (conf.nodes != null) {
            for (Object node : conf.nodes) {
                Map<String,Object> mapNode = (Map<String,Object>)node;
                String name = (String)mapNode.get("name");
                String icon = (String)mapNode.get("icon");
                URL iconUrl = TabbedToolsPane.class.getResource(icon);
                if (iconUrl == null) {
                    continue;
                }
                
                Integer size = (Integer)mapNode.get("size");
                if (size == null) size = 50;
                Map<String,Object> style = (Map<String,Object>)mapNode.get("style");

                mxCell tpl = palette.addTemplate(name, new ImageIcon(iconUrl), 
                        styleString(style), size, size, name);
                if (Boolean.FALSE.equals(mapNode.get("connectable"))) {
                    tpl.setConnectable(false);
                }
            }
        }
        
        if (conf.edges != null) {
            for (Object edge : conf.edges) {
                Map<String,Object> mapEdge = (Map<String,Object>)edge;
                String name = (String)mapEdge.get("name");
                String icon = (String)mapEdge.get("icon");
                URL iconUrl = TabbedToolsPane.class.getResource(icon);
                if (iconUrl == null) {
                    continue;
                }
                
                Map<String,Object> style = (Map<String,Object>)mapEdge.get("style");
                palette.addEdgeTemplate(name, new ImageIcon(iconUrl),
                        styleString(style), 80, 80, name);                
            }
        }
    }
    
    private String styleString(Map<String,Object> style) {
        if (style == null || style.isEmpty()) {
            return null;
        }
        
        List<String> items = new ArrayList<String>(style.size());
        for (Map.Entry<String,Object> entry : style.entrySet()) {
            items.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return String.join(";", items);
    }
}
