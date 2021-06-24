package wjl.demo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowFrameAction extends AbstractAction {
    private final JFrame frame;

    public ShowFrameAction(String menuName, JFrame frame) {
        super(menuName);
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(true);
    }
}
