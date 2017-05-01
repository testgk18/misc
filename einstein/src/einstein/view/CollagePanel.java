/*
 * Created on 06.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;

import einstein.Collage;
import einstein.Entry;
import einstein.Main;
import einstein.unredo.ActiveLayerUnredo;
import einstein.unredo.DeleteUnredo;
import einstein.unredo.MoveUnredo;

/**
 * @author zoppke
 * 
 * TODO
 */
public class CollagePanel extends JPanel implements Observer, MouseListener,
        MouseMotionListener {

    Collage collage;

    Main main;

    private Point clickPoint;

    private Point oldPoint;

    public CollagePanel(Collage collage, Main main) {
        this.collage = collage;
        this.main = main;
    }

    public void init() {
        collage.addObserver(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        repaint();
    }

    public void paint(Graphics g) {

        // draw background
        g.setColor(collage.getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // draw images
        Vector entries = collage.getEntries();
        Vector points = collage.getPoints();
        for (int i = points.size() - 1; i >= 0; --i) {
            Point p = (Point) points.get(i);
            Image img = ((Entry) entries.get(i)).getFile();
            g.drawImage(img, p.x, p.y, this);
        }

        // draw rectangle highlighting the active image
        int activeLayer = collage.getActiveLayer();
        if (activeLayer >= 0 && activeLayer < points.size()) {
            Point p = (Point) points.get(activeLayer);
            Image img = ((Entry) entries.get(activeLayer)).getFile();
            g.setColor(Color.BLUE);
            g.drawRect(p.x, p.y, img.getWidth(this), img.getHeight(this));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {

        // check the active layer first
        Vector points = collage.getPoints();
        int activeLayer = collage.getActiveLayer();
        if (activeLayer >= 0 && activeLayer < points.size()) {
            if (checkIfHits(activeLayer, e)) {
                return;
            }
        }

        // iterate on all images
        Vector entries = collage.getEntries();
        for (int i = 0; i < points.size(); ++i) {
            if (checkIfHits(i, e)) {
                return;
            }
        }
    }

    private boolean checkIfHits(int layer, MouseEvent e) {
        Vector points = collage.getPoints();
        Vector entries = collage.getEntries();

        // check, if we are hitting any image
        Point p = (Point) points.get(layer);
        Entry entry = (Entry) entries.get(layer);
        Image img = entry.getFile();
        Rectangle rect = new Rectangle(p.x, p.y, img.getWidth(this), img
                .getHeight(this));
        if (rect.contains(e.getPoint())) {

            // we are hitting an image. Save its old point.
            clickPoint = e.getPoint();
            oldPoint = new Point(p);
            int oldlayer = collage.getActiveLayer();
            if (layer != oldlayer) {
                collage.setActiveLayer(layer);
                main.addUnredo(new ActiveLayerUnredo(oldlayer, layer, collage));
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        clickPoint = null;
        int activeLayer = collage.getActiveLayer();
        Entry entry = (Entry) collage.getEntries().get(
                activeLayer);
        int width = entry.getFile().getWidth(this);
        int height = entry.getFile().getHeight(this);
        Point p = (Point) collage.getPoints().get(activeLayer);
        if (p.x + width < 0 || p.y + height < 0 || p.x > getWidth()
                || p.y > getHeight()) {
            collage.delete();
            main.addUnredo(new DeleteUnredo(activeLayer, entry, p, collage));
        } else {
            main.addUnredo(new MoveUnredo(oldPoint, p, collage));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        if (clickPoint != null) {
            Point ep = e.getPoint();
            int x = ep.x - clickPoint.x + oldPoint.x;
            int y = ep.y - clickPoint.y + oldPoint.y;
            Point p = (Point) collage.getPoints().get(collage.getActiveLayer());
            p.setLocation(x, y);
            repaint();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * @return
     */
    public BufferedImage getImage() {
        // create buffered image from panel
        BufferedImage img = new BufferedImage(getWidth(), getHeight(),
                ColorSpace.TYPE_RGB);
        Graphics g = img.getGraphics();
        int layer = collage.getActiveLayer();
        collage.setActiveLayer(-1);
        paint(g);
        collage.setActiveLayer(layer);
        return img;
    }

}