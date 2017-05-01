/*
 * Created on 21.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.unredo;

import java.awt.Point;

import einstein.Collage;

/**
 * @author zoppke
 * 
 * TODO
 */
public class MoveUnredo implements Unredo {

    private Point oldPoint;

    private Point newPoint;

    private Collage collage;

    /**
     * @param oldPoint
     * @param p
     */
    public MoveUnredo(Point oldPoint, Point newPoint, Collage collage) {
        this.oldPoint = oldPoint;
        this.newPoint = newPoint;
        this.collage = collage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see einstein.unredo.Unredo#undo()
     */
    public void undo() {
        collage.setPoint(oldPoint, collage.getActiveLayer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see einstein.unredo.Unredo#redo()
     */
    public void redo() {
        collage.setPoint(newPoint, collage.getActiveLayer());
    }

}