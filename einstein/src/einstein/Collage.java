/*
 * Created on 07.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Observable;
import java.util.Vector;

import einstein.view.ImagePanel;

/**
 * @author zoppke
 * 
 * TODO
 */
public class Collage extends Observable {

    public static final String IMAGE_ADDED = "IMAGE_ADDED";

    public static final String ACTIVE_LAYER_CHANGED = "ACTIVE_LAYER_CHANGED";

    /////////////////////////////// fields ////////////////////////////////////

    // the currently active layer to be highlighted
    private int activeLayer = -1;

    // the background color
    private Color background = Color.WHITE;

    // a list of image panels representing the images to be painted
    // lowest index = upmost layer
    private Vector entries = new Vector();

    // a list of points corresponding to the list of image panels
    // lowest index = upmost layer
    private Vector points = new Vector();

    ////////////////////////////// constructor ////////////////////////////////

    public Collage() {
    }

    /**
     * @param draggingImagePanel
     * @param newPoint
     */
    public void addEntry(Entry entry, Point point) {
        entries.add(0, entry);
        points.add(0, point);
        setChanged();
        notifyObservers(IMAGE_ADDED);
        clearChanged();
        setActiveLayer(0);
    }

    /**
     * @return Returns the imagePanels.
     */
    public Vector getEntries() {
        return entries;
    }

    /**
     * @return Returns the points.
     */
    public Vector getPoints() {
        return points;
    }

    /**
     * @return Returns the background.
     */
    public Color getBackground() {
        return background;
    }

    /**
     * @return Returns the activeLayer.
     */
    public int getActiveLayer() {
        return activeLayer;
    }

    /**
     * @param activeLayer
     *            The activeLayer to set.
     */
    public void setActiveLayer(int activeLayer) {
        if (this.activeLayer != activeLayer) {
            this.activeLayer = activeLayer;
            setChanged();
            notifyObservers(ACTIVE_LAYER_CHANGED);
            clearChanged();
        }
    }

    public void pushDown() {

        // swap panels and points
        entries.add(activeLayer + 1, entries.remove(activeLayer));
        points.add(activeLayer + 1, points.remove(activeLayer));
        setChanged();
        notifyObservers(IMAGE_ADDED);
        clearChanged();
        setActiveLayer(activeLayer + 1);
    }

    public void pushUp() {

        // swap panels and points
        entries.add(activeLayer - 1, entries.remove(activeLayer));
        points.add(activeLayer - 1, points.remove(activeLayer));
        setChanged();
        notifyObservers(IMAGE_ADDED);
        clearChanged();
        setActiveLayer(activeLayer - 1);
    }

    public void delete() {
        entries.remove(activeLayer);
        points.remove(activeLayer);
        setChanged();
        notifyObservers(IMAGE_ADDED);
        clearChanged();
        //setActiveLayer(activeLayer - 1);
    }

    /**
     * @param layer
     * @return
     */
    public Entry getEntry(int layer) {
        return (Entry) entries.get(layer);
    }

    /**
     * @param layer
     * @return
     */
    public Point getPoint(int layer) {
        return (Point) points.get(layer);
    }

    public void setPoint(Point p, int layer) {
        points.set(layer, p);
        setChanged();
        notifyObservers(IMAGE_ADDED);
        clearChanged();
    }
}