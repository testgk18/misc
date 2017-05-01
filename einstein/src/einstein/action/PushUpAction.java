/*
 * Created on 13.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.action;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import einstein.Collage;
import einstein.Main;
import einstein.unredo.PushUpUnredo;

/**
 * @author zoppke
 * 
 * TODO
 */
public class PushUpAction extends AbstractAction implements Observer {

    private Collage collage;
    private Main main;

    public PushUpAction(Main main, Collage collage) {
        this.collage = collage;
        this.main=main;
        //putValue(Action.NAME, "hoch");
        putValue(Action.SHORT_DESCRIPTION, "Eine Ebene nach oben");
        Icon icon = new ImageIcon(main.getResourceAsImage("images/up.gif"));
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
        collage.pushUp();
        main.addUnredo(new PushUpUnredo(collage));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        int activeLayer = collage.getActiveLayer();
        setEnabled(0 < activeLayer
                && activeLayer < collage.getPoints().size());
    }

}