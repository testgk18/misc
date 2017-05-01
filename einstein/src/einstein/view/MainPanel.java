/*
 * Created on 06.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import einstein.Entry;
import einstein.Main;

/**
 * @author zoppke
 * 
 * TODO
 */
public class MainPanel extends JPanel {

    private Main main;

    //////////////////////////// view components //////////////////////////////

    // left scrollpane, containing the timeline Panels
    private JScrollPane scrollPane;

    // einstein timeline panel
    private TimelinePanel einsteinPanel;

    // collage panel
    private RightPanel rightPanel;

    public MainPanel(Main main) {
        this.main = main;
    }

    public void init() {

        // set layout
        setLayout(null);

        // create panels
        einsteinPanel = new TimelinePanel(main.getTimeline(), main);
        rightPanel = new RightPanel(main.getCollage(), main);

        // init panels
        einsteinPanel.init();
        rightPanel.init();

        // create and init scrollpane
        scrollPane = new JScrollPane(einsteinPanel);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        if (vBar != null) {
            vBar.setBackground(main.getBGColor());
        }

        // add components
        add(scrollPane);
        add(rightPanel);

        // set bounds
        scrollPane.setBounds(0, 0, 390, 560);
        rightPanel.setBounds(390, 0, 360, 560);
    }

    public void update(Graphics g) {
        paint(g);
        paintChildren(g);
        paintDraggingImage(g);
    }

    /**
     * @param g
     */
    public void paintDraggingImage(Graphics g) {
        if (main.getDragEntry() != null) {
            Entry entry = main.getDragEntry();
            int width = entry.getFile().getWidth(this);
            int height = entry.getFile().getHeight(this);
            Point p = main.getDraggingPoint();
            g.setXORMode(Color.BLUE);
            g.drawImage(entry.getFile(), p.x, p.y, this);
            g.setPaintMode();
        }
    }

    public static Point getLocationOnMainPanel(Component c) {
        Point p = new Point(0, 0);
        do {
            Point q = c.getLocation();
            p.translate(q.x, q.y);
            c = c.getParent();
        } while (!(c instanceof MainPanel));
        return p;
    }

    /**
     * @return Returns the rightPanel.
     */
    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public CollagePanel getCollagePanel() {
        return rightPanel.getCollagePanel();
    }

}