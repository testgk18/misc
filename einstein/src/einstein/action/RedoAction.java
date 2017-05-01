/*
 * Created on 18.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import einstein.Main;
import einstein.unredo.UnredoListener;

/**
 * @author zoppke
 *
 * TODO
 */
public class RedoAction extends AbstractAction implements UnredoListener{
    private Main main;
    
    public RedoAction(Main main) {
        this.main=main;
        putValue(Action.SHORT_DESCRIPTION, "Wiederherstellen");
        Icon icon = new ImageIcon(main.getResourceAsImage("images/redo.gif"));
        putValue(Action.SMALL_ICON, icon);
        setEnabled(false);
        main.addUnredoListener(this);
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        main.redo();
    }

    /* (non-Javadoc)
     * @see einstein.unredo.UnredoListener#unredoChanged(boolean, boolean)
     */
    public void unredoChanged(boolean undo, boolean redo) {
        setEnabled(redo);
    }


}
