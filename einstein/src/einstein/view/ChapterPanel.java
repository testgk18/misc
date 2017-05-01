/*
 * Created on 05.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.JPanel;

import einstein.Chapter;
import einstein.Entry;
import einstein.Main;

/**
 * @author zoppke
 * 
 * TODO
 */
public class ChapterPanel extends JPanel implements MouseListener,
        MouseMotionListener {

    private Main main;

    private Chapter chapter;

    private Point clickPoint = null;

    /**
     * @param entry
     */
    public ChapterPanel(Chapter chapter, Main main) {
        this.chapter = chapter;
        this.main = main;
    }

    public void init() {

        // set Layout
        setLayout(null);
        //setBackground(main.getColor());
        //setDoubleBuffered(true);

        // add myself as listener
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paint(Graphics g) {
        // iterate on entries
        Iterator iter = chapter.getEntries().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            g.drawImage(entry.getFile(), entry.getX(), entry.getY(), this);
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
        Iterator iter = chapter.getEntries().iterator();
        Rectangle r = new Rectangle();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            if (entry.isText()) {
                continue;
            }
            r.x = entry.getX();
            r.y = entry.getY();
            r.width = entry.getFile().getWidth(this);
            r.height = entry.getFile().getHeight(this);
            if (r.contains(e.getPoint())) {
                clickPoint = e.getPoint();
                main.setDragEntry(entry);
                Point p = MainPanel.getLocationOnMainPanel(this);
                p.x += r.x;
                p.y += r.y;
                main.setDraggingPoint(p);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        if (clickPoint != null) {
            clickPoint = null;
            main.setDragEntry(null);
            main.setDraggingPoint(null);
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
            Point lp = MainPanel.getLocationOnMainPanel(this);
            Entry entry = main.getDragEntry();
            int x = ep.x - clickPoint.x + lp.x + entry.getX();
            int y = ep.y - clickPoint.y + lp.y + entry.getY();
            main.setDraggingPoint(new Point(x, y));
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
}