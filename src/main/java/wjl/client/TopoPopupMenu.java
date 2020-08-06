package wjl.client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;

public class TopoPopupMenu extends JPopupMenu {
    private static final long serialVersionUID = -1271139587867605312L;
    private final mxGraphComponent component;
    private final JMenuItem deleteMenu;

    public TopoPopupMenu(mxGraphComponent graphComponent) {
        component = graphComponent;

        // 右键删除选中的对象
        deleteMenu = add(bind("delete", "/images/delete.gif", 
                mxGraphActions.getDeleteAction()));
    }
    
    @SuppressWarnings("serial")
    private Action bind(String name, String iconUrl, final Action action)
    {
        AbstractAction newAction = new AbstractAction(name, 
                (iconUrl != null) ? new ImageIcon(TopoPopupMenu.class.getResource(iconUrl)) : null)
        {
            public void actionPerformed(ActionEvent e) {
                e.setSource(component);
                action.actionPerformed(e);
            }
        };
        
        newAction.putValue(Action.SHORT_DESCRIPTION, 
                action.getValue(Action.SHORT_DESCRIPTION));
        return newAction;
    }
    
    public void refreshMenuState() {
        boolean selected = !component.getGraph().isSelectionEmpty();
        deleteMenu.setEnabled(selected);
    }
}
