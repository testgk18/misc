/*
 * Created on 13.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.action;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import einstein.Collage;
import einstein.Entry;
import einstein.Main;
import einstein.unredo.DeleteUnredo;

/**
 * @author zoppke
 * 
 * TODO
 */
public class DeleteAction extends AbstractAction implements Observer {

    private Collage collage;

    private Main main;

    public DeleteAction(Main main, Collage collage) {
        this.collage = collage;
        this.main = main;
        //putValue(Action.NAME, "Entf");
        putValue(Action.SHORT_DESCRIPTION, "Ebene entfernen");
        Icon icon = new ImageIcon(main.getResourceAsImage("images/delete.gif"));
        putValue(Action.SMALL_ICON, icon);
        setEnabled(false);
        collage.addObserver(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        int layer = collage.getActiveLayer();
        Entry entry = collage.getEntry(layer);
        Point p = collage.getPoint(layer);
        main.addUnredo(new DeleteUnredo(layer, entry,p, collage));
        collage.delete();
    }

    public void update(Observable o, Object arg) {
        int activeLayer = collage.getActiveLayer();
        setEnabled(0 <= activeLayer && activeLayer < collage.getPoints().size());
    }

}