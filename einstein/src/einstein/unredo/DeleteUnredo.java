/*
 * Created on 17.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.unredo;

import java.awt.Point;

import einstein.Collage;
import einstein.Entry;

/**
 * @author zoppke
 * 
 * TODO
 */
public class DeleteUnredo implements Unredo {

    private Entry entry;

    private int layer;

    private Point point;

    private Collage collage;

    public DeleteUnredo(int layer, Entry entry, Point point,
            Collage collage) {
        this.layer = layer;
        this.entry = entry;
        this.point = point;
        this.collage = collage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see einstein.unredo.Unredo#undo()
     */
    public void undo() {
        collage.addEntry(entry, point);
    }

    /*
     * (non-Javadoc)
     * 
     * @see einstein.unredo.Unredo#redo()
     */
    public void redo() {
        collage.delete();
    }

}