/*
 * Created on 06.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import einstein.Main;

/**
 * @author zoppke
 * 
 * TODO
 */
public class ImagePanel extends JPanel implements MouseListener,
        MouseMotionListener {

    // the image to be displayed
    private Image image;

    // reference to main class
    private Main main;

    // point where we clicked the panel
    private Point clickPoint = null;

    private String name;

    public ImagePanel(Image image, Main main, String name) {
        this.image = image;
        this.main = main;
        this.name = name;
    }

    public void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Dimension getPreferredSize() {
        if (image == null) {
            return new Dimension(0, 0);
        }
        int width = image.getWidth(this);
        int height = image.getHeight(this);
        return new Dimension(width, height);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
     //return new Dimension(150,150);
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
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
        clickPoint = e.getPoint();
        //main.setDragEntry(this);
        main.setDraggingPoint(MainPanel.getLocationOnMainPanel(this));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        clickPoint = null;
        main.setDragEntry(null);
        main.setDraggingPoint(null);
    }

    /**
     * @return Returns the image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image
     *            The image to set.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        Point ep = e.getPoint();
        Point lp = MainPanel.getLocationOnMainPanel(this);
        int x = ep.x - clickPoint.x + lp.x;
        int y = ep.y - clickPoint.y + lp.y;
        main.setDraggingPoint(new Point(x, y));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public String toString() {
        return name;
    }
}