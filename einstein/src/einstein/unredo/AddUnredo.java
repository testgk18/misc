/*
 * Created on 18.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.unredo;

import java.awt.Point;

import einstein.Collage;
import einstein.Entry;
import einstein.view.ImagePanel;

/**
 * @author zoppke
 * 
 * TODO
 */
public class AddUnredo implements Unredo {

    private Entry entry;

    private Point point;

    private int layer;

    private Collage collage;

    /**
     * @param panel
     * @param point
     * @param layer
     * @param collage
     */
    public AddUnredo(Entry entry, Point point, int layer, Collage collage) {
        super();
        this.entry = entry;
        this.point = point;
        this.layer = layer;
        this.collage = collage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see einstein.unredo.Unredo#undo()
     */
    public void undo() {
        collage.delete();
    }

    /*
     * (non-Javadoc)
     * 
     * @see einstein.unredo.Unredo#redo()
     */
    public void redo() {
        collage.addEntry(entry, point);
    }

}