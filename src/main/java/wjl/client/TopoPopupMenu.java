package wjl.client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;

public class TopoPopupMenu extends JPopupMenu {
    private static final long serialVersionUID = -1271139587867605312L;
    private mxGraphComponent component;
    public TopoPopupMenu(mxGraphComponent graphComponent) {
        component = graphComponent;
        boolean selected = !component.getGraph().isSelectionEmpty();
        JMenu menu;
        
        // 右键删除选中的对象
        add(bind("delete", 
                mxGraphActions.getDeleteAction(),
                "/images/delete.gif"))
            .setEnabled(selected);
    }
    
    @SuppressWarnings("serial")
    public Action bind(String name, final Action action, String iconUrl)
    {
        AbstractAction newAction = new AbstractAction(name, 
                (iconUrl != null) ? new ImageIcon(TopoPopupMenu.class.getResource(iconUrl)) : null)
        {
            public void actionPerformed(ActionEvent e)
            {
                action.actionPerformed(new ActionEvent(component, e
                        .getID(), e.getActionCommand()));
            }
        };
        
        newAction.putValue(Action.SHORT_DESCRIPTION, 
                action.getValue(Action.SHORT_DESCRIPTION));
        return newAction;
    }
}
